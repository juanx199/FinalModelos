package com.mycompany.carmotor.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.service.BankEntityService;
import com.mycompany.carmotor.service.BranchService;
import com.mycompany.carmotor.service.TestDriveService;
import com.mycompany.carmotor.service.VehicleService;

@Controller
@RequestMapping("/")
public class VehicleController {

    private final VehicleService vehicleService;
    private final BankEntityService bankEntityService;
    private final BranchService branchService;
    private final TestDriveService testDriveService;

    public VehicleController(VehicleService vehicleService,
            BankEntityService bankEntityService,
            BranchService branchService,
            TestDriveService testDriveService) {
        this.vehicleService = vehicleService;
        this.bankEntityService = bankEntityService;
        this.branchService = branchService;
        this.testDriveService = testDriveService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type,
            @RequestParam(name = "model", required = false) String modelYear,
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String insurable,
            @RequestParam(required = false) String availableOnly,
            @RequestParam(required = false) String priceRange,
            Model model) {

        List<Vehicle> vehicles;
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();

        boolean hasCombinedSearch = isNotBlank(brand)
                || isNotBlank(type)
                || isNotBlank(modelYear)
                || isNotBlank(plate)
                || isNotBlank(insurable)
                || isNotBlank(priceRange);

        if (hasCombinedSearch) {
            Integer modelValue = parseNullableInt(modelYear);
            Integer plateValue = parseNullableInt(plate);
            Boolean insurableValue = parseNullableBoolean(insurable);
            vehicles = vehicleService.searchByCriteria(
                    brand, type, modelValue, plateValue, insurableValue, priceRange);
        } else if (searchType != null && searchValue != null && !searchValue.isBlank()) {

            try {
                vehicles = switch (searchType) {
                    case "brand" ->
                        vehicleService.searchByBrand(searchValue);
                    case "type" ->
                        vehicleService.searchByType(searchValue);
                    case "plate" ->
                        vehicleService.searchByLastDigitPlate(Integer.parseInt(searchValue));
                    case "model" ->
                        vehicleService.searchByModel(Integer.parseInt(searchValue)); // ← int
                    default ->
                        vehicleService.getAllVehicles();
                };
            } catch (NumberFormatException e) {
                // Si escriben texto donde va un número, devuelve todo
                vehicles = vehicleService.getAllVehicles();
            }
        } else {
            vehicles = vehicleService.getAllVehicles();
        }

        Boolean availableOnlyValue = parseNullableBoolean(availableOnly);
        if (Boolean.TRUE.equals(availableOnlyValue)) {
            vehicles = vehicles.stream()
                    .filter(v -> "AVAILABLE".equals(v.getStateName()))
                    .toList();
        }

        vehicles = sortVehicles(vehicles, sortBy);

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("brand", brand);
        model.addAttribute("type", type);
        model.addAttribute("modelYear", modelYear);
        model.addAttribute("plate", plate);
        model.addAttribute("insurable", insurable);
        model.addAttribute("availableOnly", availableOnly);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("brandOptions", getBrandOptions(allVehicles));
        model.addAttribute("typeOptions", getTypeOptions(allVehicles));
        model.addAttribute("modelOptions", getModelOptions(allVehicles));
        model.addAttribute("plateOptions", getPlateOptions(allVehicles));
        model.addAttribute("insurableOptions", getInsurableOptions());
        model.addAttribute("priceRangeOptions", getPriceRangeOptions());
        model.addAttribute("banks", bankEntityService.getAllBankEntities());
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("schedulers", testDriveService.getSchedulers());
        return "index";
    }

    private Integer parseNullableInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Boolean parseNullableBoolean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Boolean.valueOf(value);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private List<Vehicle> sortVehicles(List<Vehicle> vehicles, String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return vehicles;
        }

        Comparator<Vehicle> comparator = switch (sortBy) {
            case "priceAsc" -> Comparator.comparingDouble(Vehicle::getPrice);
            case "priceDesc" -> Comparator.comparingDouble(Vehicle::getPrice).reversed();
            case "model", "modelDesc" -> Comparator.comparingInt(Vehicle::getModel).reversed();
            case "modelAsc" -> Comparator.comparingInt(Vehicle::getModel);
            case "capacity" -> Comparator.comparingInt(Vehicle::getPassengerCapacity);
            default -> null;
        };

        if (comparator == null) {
            return vehicles;
        }

        return vehicles.stream()
                .sorted(comparator)
                .toList();
    }

    private List<String> getBrandOptions(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getBrand)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private List<String> getTypeOptions(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(v -> v.getType().toString())
                .filter(Objects::nonNull)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private List<Integer> getModelOptions(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getModel)
                .distinct()
                .sorted()
                .toList();
    }

    private List<Integer> getPlateOptions(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getLastDigitPlate)
                .distinct()
                .sorted()
                .toList();
    }

    private List<FilterOption> getInsurableOptions() {
        return List.of(
                new FilterOption("true", "Asegurable"),
                new FilterOption("false", "No asegurable")
        );
    }

    private List<FilterOption> getPriceRangeOptions() {
        return List.of(
                new FilterOption("0_50000000", "Hasta $50M"),
                new FilterOption("50000001_80000000", "$50M - $80M"),
                new FilterOption("80000001_110000000", "$80M - $110M"),
                new FilterOption("110000001_plus", "Mas de $110M")
        );
    }

    private record FilterOption(String value, String label) {}

    @GetMapping("/vehicle/{id}/negotiate")
    public String startNegotiation(@PathVariable Long id) {
        vehicleService.startNegotiation(id);
        return "redirect:/vehicle/" + id;
    }

    @GetMapping("/vehicle/{id}/confirm-sale")
    public String confirmSale(@PathVariable Long id) {
        vehicleService.confirmSale(id);
        return "redirect:/vehicle/" + id;
    }

    @GetMapping("/vehicle/{id}/cancel-negotiation")
    public String cancelNegotiation(@PathVariable Long id) {
        vehicleService.cancelNegotiation(id);
        return "redirect:/vehicle/" + id;
    }

    // Mostrar slots disponibles en el detalle del vehículo
@GetMapping("/vehicle/{id}")
    public String vehicleDetail(@PathVariable Long id,
            @RequestParam(required = false) Boolean tdBooked,
            Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("maintenance", vehicleService.getMaintenanceHistory(id));
        model.addAttribute("insuranceQuotes", vehicleService.getInsuranceQuotes(id));
        model.addAttribute("banks", bankEntityService.getAllBankEntities());
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("schedulers", testDriveService.getSchedulers());
        model.addAttribute("tdBooked", tdBooked);
        return "vehicle-detail";
    }

    // Agrega estos endpoints
    @PostMapping("/vehicle/{id}/schedule-testdrive")
    public String scheduleTestDrive(@PathVariable Long id,
            @RequestParam String branchName,
            @RequestParam String slotId,
            @RequestParam String clientName,
            @RequestParam String clientEmail,
            @RequestParam String clientPhone,
            Model model) {
        boolean success = testDriveService.scheduleTestDrive(id, branchName, slotId, clientName, clientEmail, clientPhone);
        model.addAttribute("tdSuccess", success);
        model.addAttribute("tdBranch", branchName);
        return "redirect:/vehicle/" + id + "?tdBooked=" + success;
    }
}
