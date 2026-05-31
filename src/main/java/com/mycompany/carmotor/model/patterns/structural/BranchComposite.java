package com.mycompany.carmotor.model.patterns.structural;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class BranchComposite {

    private String name;
    private String address;
    private String phone;
    private String businessHours;
    private List<String> testDriveSlots = new ArrayList<>();
    private List<BranchComposite> subBranches = new ArrayList<>();
    private List<IVehicle> vehicles = new ArrayList<>();

    public BranchComposite(String name) {
        this(name, "No address", "No phone", "No hours");
    }

    public BranchComposite(String name, String address, String phone, String businessHours) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.businessHours = businessHours;
    }

    public void add(BranchComposite branch) { subBranches.add(branch); }
    public void remove(BranchComposite branch) { subBranches.remove(branch); }

    public void addVehicle(IVehicle vehicle) { vehicles.add(vehicle); }
    public void addTestDriveSlot(String slot) { testDriveSlots.add(slot); }

    public void showStructure(String prefix) {
        System.out.println(prefix + "|-- " + name +
                " (" + address + ", Tel: " + phone + ", Hours: " + businessHours + ")");
        if (!testDriveSlots.isEmpty())
            System.out.println(prefix + "    [Test Drive slots: " + testDriveSlots + "]");
        if (!vehicles.isEmpty()) {
            System.out.print(prefix + "    [Vehicles: ");
            for (int i = 0; i < vehicles.size(); i++) {
                IVehicle v = vehicles.get(i);
                System.out.print(v.getBrand() + " " + v.getModel() + " ($" + v.getPrice() + ")");
                if (i < vehicles.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        for (BranchComposite sub : subBranches) sub.showStructure(prefix + "    ");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }
    public List<String> getTestDriveSlots() { return testDriveSlots; }
    public List<BranchComposite> getSubBranches() { return subBranches; }
    public List<IVehicle> getVehicles() { return vehicles; }

    public void setTestDriveSlots(List<String> testDriveSlots) {
        this.testDriveSlots = testDriveSlots;
    }

    public void setSubBranches(List<BranchComposite> subBranches) {
        this.subBranches = subBranches;
    }

    public void setVehicles(List<IVehicle> vehicles) {
        this.vehicles = vehicles;
    }
}