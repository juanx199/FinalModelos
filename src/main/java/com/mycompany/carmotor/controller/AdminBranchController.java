package com.mycompany.carmotor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.carmotor.model.domain.Branch;
import com.mycompany.carmotor.service.BranchService;

@Controller
@RequestMapping("/admin/branches")
public class AdminBranchController {

    private final BranchService branchService;

    public AdminBranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String listBranches(Model model) {
        model.addAttribute("branches", branchService.getAllBranchesFromDb());
        return "admin/branches";
    }

    @GetMapping("/new")
    public String newBranchForm(Model model) {
        Branch branch = new Branch();
        prepareForm(model, branch, false, null);
        return "admin/branch-form";
    }

    @PostMapping
    public String createBranch(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String businessHours,
            @RequestParam String testDriveSlotsStr,
            Model modelView) {

        Branch branch = new Branch(name, address, phone, businessHours);
        String error = applySlots(branch, testDriveSlotsStr);
        if (error != null) {
            prepareForm(modelView, branch, false, error);
            return "admin/branch-form";
        }

        branchService.saveBranch(branch);
        return "redirect:/admin/branches";
    }

    @GetMapping("/{id}/edit")
    public String editBranchForm(@PathVariable Long id, Model model) {
        Branch branch = branchService.getBranchById(id);
        prepareForm(model, branch, true, null);
        return "admin/branch-form";
    }

    @PostMapping("/{id}")
    public String updateBranch(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String businessHours,
            @RequestParam String testDriveSlotsStr,
            Model modelView) {

        Branch branch = branchService.getBranchById(id);
        branch.setName(name);
        branch.setAddress(address);
        branch.setPhone(phone);
        branch.setBusinessHours(businessHours);

        String error = applySlots(branch, testDriveSlotsStr);
        if (error != null) {
            prepareForm(modelView, branch, true, error);
            return "admin/branch-form";
        }

        branchService.saveBranch(branch);
        return "redirect:/admin/branches";
    }

    @PostMapping("/{id}/delete")
    public String deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return "redirect:/admin/branches";
    }

    private void prepareForm(Model model, Branch branch, boolean isEdit, String errorMessage) {
        model.addAttribute("branch", branch);
        
        StringBuilder sb = new StringBuilder();
        if (branch.getTestDriveSlots() != null) {
            for (String slot : branch.getTestDriveSlots()) {
                sb.append(slot).append("\n");
            }
        }
        model.addAttribute("testDriveSlotsStr", sb.toString().trim());
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("formAction", isEdit
                ? "/admin/branches/" + branch.getId()
                : "/admin/branches");
        model.addAttribute("errorMessage", errorMessage);
    }

    private String applySlots(Branch branch, String testDriveSlotsStr) {
        branch.getTestDriveSlots().clear();
        if (testDriveSlotsStr != null && !testDriveSlotsStr.isBlank()) {
            String[] lines = testDriveSlotsStr.split("\\r?\\n");
            for (String line : lines) {
                if (!line.trim().isBlank()) {
                    branch.getTestDriveSlots().add(line.trim());
                }
            }
        }
        return null;
    }
}
