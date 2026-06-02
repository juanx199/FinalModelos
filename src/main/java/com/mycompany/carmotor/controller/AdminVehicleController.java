package com.mycompany.carmotor.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.Photo;
import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.VehicleType;
import com.mycompany.carmotor.service.AdvisorService;
import com.mycompany.carmotor.service.BranchService;
import com.mycompany.carmotor.service.VehicleService;

@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {
    
    private static final Path UPLOAD_DIR = Paths.get("src/main/resources/static/img/uploads").toAbsolutePath();
    private static final String UPLOAD_URL_PREFIX = "/img/uploads/";

    private final VehicleService vehicleService;
    private final AdvisorService advisorService;
    private final BranchService branchService;

    public AdminVehicleController(VehicleService vehicleService, AdvisorService advisorService, BranchService branchService) {
        this.vehicleService = vehicleService;
        this.advisorService = advisorService;
        this.branchService = branchService;
    }

    @GetMapping
    public String listVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "admin/vehicles";
    }

    @GetMapping("/new")
    public String newVehicleForm(Model model) {
        Vehicle vehicle = new Vehicle();
        prepareForm(model, vehicle, false, null);
        return "admin/vehicle-form";
    }

    @PostMapping
    public String createVehicle(
            @RequestParam String brand,
            @RequestParam String type,
            @RequestParam String model,
            @RequestParam String price,
            @RequestParam String passengerCapacity,
            @RequestParam String lastDigitPlate,
            @RequestParam String advisorId,
            @RequestParam String insurable,
            @RequestParam(required = false) Long branchId,
            @RequestParam("photos") MultipartFile[] photos,
            Model modelView) {

        Vehicle vehicle = new Vehicle();
        String error = applyFormData(vehicle, brand, type, model, price,
                passengerCapacity, lastDigitPlate, advisorId, insurable, photos, true);
        if (error != null) {
            prepareForm(modelView, vehicle, false, error);
            return "admin/vehicle-form";
        }

        List<String> photoPaths = storePhotos(photos);
        if (photoPaths.isEmpty()) {
            prepareForm(modelView, vehicle, false, "Debe subir al menos una foto valida.");
            return "admin/vehicle-form";
        }
        applyPhotos(vehicle, photoPaths);

        if (branchId != null) {
            vehicle.setBranch(branchService.getBranchById(branchId));
        }

        vehicleService.saveVehicle(vehicle);
        branchService.refreshBranchSystem();
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/{id}/edit")
    public String editVehicleForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        prepareForm(model, vehicle, true, null);
        return "admin/vehicle-form";
    }

    @PostMapping("/{id}")
    public String updateVehicle(
            @PathVariable Long id,
            @RequestParam String brand,
            @RequestParam String type,
            @RequestParam String model,
            @RequestParam String price,
            @RequestParam String passengerCapacity,
            @RequestParam String lastDigitPlate,
            @RequestParam String advisorId,
            @RequestParam String insurable,
            @RequestParam(required = false) Long branchId,
            @RequestParam("photos") MultipartFile[] photos,
            Model modelView) {

        Vehicle vehicle = vehicleService.getVehicleById(id);
        boolean requirePhotos = vehicle.getPhotos().isEmpty();

        String error = applyFormData(vehicle, brand, type, model, price,
                passengerCapacity, lastDigitPlate, advisorId, insurable, photos, requirePhotos);
        if (error != null) {
            prepareForm(modelView, vehicle, true, error);
            return "admin/vehicle-form";
        }

        if (hasUploadedFiles(photos)) {
            List<String> photoPaths = storePhotos(photos);
            if (photoPaths.isEmpty()) {
                prepareForm(modelView, vehicle, true, "No se pudieron guardar las fotos.");
                return "admin/vehicle-form";
            }
            applyPhotos(vehicle, photoPaths);
        }

        if (branchId != null) {
            vehicle.setBranch(branchService.getBranchById(branchId));
        } else {
            vehicle.setBranch(null);
        }

        vehicleService.saveVehicle(vehicle);
        branchService.refreshBranchSystem();
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        branchService.refreshBranchSystem();
        return "redirect:/admin/vehicles";
    }

    private void prepareForm(Model model, Vehicle vehicle, boolean isEdit, String errorMessage) {
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("advisors", advisorService.getAllAdvisors());
        model.addAttribute("branches", branchService.getAllBranchesFromDb());
        model.addAttribute("typeOptions", VehicleType.values());
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("formAction", isEdit
                ? "/admin/vehicles/" + vehicle.getId()
                : "/admin/vehicles");
        model.addAttribute("errorMessage", errorMessage);
    }

    private String applyFormData(Vehicle vehicle,
            String brand,
            String type,
            String model,
            String price,
            String passengerCapacity,
            String lastDigitPlate,
            String advisorId,
            String insurable,
            MultipartFile[] photos,
            boolean requirePhotos) {

        if (isBlank(brand)) {
            return "Marca es obligatoria.";
        }
        if (isBlank(type)) {
            return "Tipo es obligatorio.";
        }

        try {
            int modelValue = parseRequiredInt(model, "Modelo");
            double priceValue = parseRequiredDouble(price, "Precio");
            int capacityValue = parseRequiredInt(passengerCapacity, "Capacidad");
            int plateValue = parseRequiredInt(lastDigitPlate, "Ultimo digito de placa");
            if (plateValue < 0 || plateValue > 9) {
                return "El ultimo digito de placa debe estar entre 0 y 9.";
            }
            boolean insurableValue = parseRequiredBoolean(insurable, "Asegurable");
            long advisorValue = parseRequiredLong(advisorId, "Asesor");

            Advisor advisor = advisorService.getAdvisorById(advisorValue);

            vehicle.setBrand(brand.trim());
            vehicle.setType(VehicleType.fromString(type));
            vehicle.setModel(modelValue);
            vehicle.setPrice(priceValue);
            vehicle.setPassengerCapacity(capacityValue);
            vehicle.setLastDigitPlate(plateValue);
            vehicle.setInsurable(insurableValue);
            vehicle.setAdvisor(advisor);
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }

        if (requirePhotos && !hasUploadedFiles(photos)) {
            return "Debe subir al menos una foto.";
        }

        return null;
    }

    private List<String> storePhotos(MultipartFile[] photos) {
        List<String> paths = new ArrayList<>();
        if (photos == null || photos.length == 0) {
            return paths;
        }

        try {
            Files.createDirectories(UPLOAD_DIR);
        } catch (IOException ex) {
            return paths;
        }

        for (MultipartFile file : photos) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String originalName = StringUtils.cleanPath(
                    Objects.requireNonNullElse(file.getOriginalFilename(), ""));
            if (originalName.isBlank()) {
                continue;
            }

            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex > -1) {
                extension = originalName.substring(dotIndex);
            }

            String filename = UUID.randomUUID() + extension;
            Path destination = UPLOAD_DIR.resolve(filename);

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
                paths.add(UPLOAD_URL_PREFIX + filename);
            } catch (IOException ex) {
                return new ArrayList<>();
            }
        }

        return paths;
    }

    private void applyPhotos(Vehicle vehicle, List<String> photoPaths) {
        vehicle.getPhotos().clear();
        for (int i = 0; i < photoPaths.size(); i++) {
            String category = switch (i) {
                case 0 -> "Exterior";
                case 1 -> "Interior";
                case 2 -> "Motor";
                default -> "Other";
            };
            Photo photo = new Photo(category, photoPaths.get(i));
            photo.setVehicle(vehicle);
            vehicle.getPhotos().add(photo);
        }
    }

    private boolean hasUploadedFiles(MultipartFile[] photos) {
        if (photos == null) {
            return false;
        }
        for (MultipartFile file : photos) {
            if (file != null && !file.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private int parseRequiredInt(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " es obligatorio.");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " debe ser un numero.");
        }
    }

    private long parseRequiredLong(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " es obligatorio.");
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " debe ser un numero.");
        }
    }

    private double parseRequiredDouble(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " es obligatorio.");
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " debe ser un numero.");
        }
    }

    private boolean parseRequiredBoolean(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " es obligatorio.");
        }
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalArgumentException(fieldName + " no es valido.");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
