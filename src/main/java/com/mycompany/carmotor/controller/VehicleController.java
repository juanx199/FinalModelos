package com.mycompany.carmotor.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            Model model) {

        List<Vehicle> vehicles;

        if (searchType != null && searchValue != null && !searchValue.isBlank()) {
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
        } else if (sortBy != null && !sortBy.isBlank()) {
            vehicles = switch (sortBy) {
                case "priceAsc" ->
                    vehicleService.getAllOrderByPriceAsc();
                case "priceDesc" ->
                    vehicleService.getAllOrderByPriceDesc();
                case "model" ->
                    vehicleService.getAllOrderByModel();
                case "capacity" ->
                    vehicleService.getAllOrderByPassengerCapacity();
                default ->
                    vehicleService.getAllVehicles();
            };
        } else {
            vehicles = vehicleService.getAllVehicles();
        }

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("banks", bankEntityService.getAllBankEntities());
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("schedulers", testDriveService.getSchedulers());
        return "index";
    }

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
    @GetMapping("/vehicle/{id}/schedule-testdrive")
    public String scheduleTestDrive(@PathVariable Long id,
            @RequestParam String branchName,
            @RequestParam String slotId,
            Model model) {
        boolean success = testDriveService.scheduleTestDrive(id, branchName, slotId);
        model.addAttribute("tdSuccess", success);
        model.addAttribute("tdBranch", branchName);
        return "redirect:/vehicle/" + id + "?tdBooked=" + success;
    }
}
