package com.mycompany.carmotor.model.domain;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.patterns.state.AvailableState;
import com.mycompany.carmotor.model.patterns.state.InNegotiationState;
import com.mycompany.carmotor.model.patterns.state.SoldState;
import com.mycompany.carmotor.model.patterns.state.VehicleState;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "vehicles")
public class Vehicle implements IVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    @Column(name = "vehicle_year")
    private int model;
    private double price;
    private VehicleType type;
    private int passengerCapacity;
    private int lastDigitPlate;
    private String stateName;
    private boolean insurable = true;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @Transient
    @Column(name = "vehicle_type")
    private VehicleState state;

    public Vehicle() {
        this.state = new AvailableState();
        this.stateName = this.state.getStateName();
    }

    @SuppressWarnings("this-escape")
    public Vehicle(String brand, int model, double price, String type,
        int passengerCapacity, int lastDigitPlate, List<String> photoPaths) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.type = VehicleType.fromString(type);
        this.passengerCapacity = passengerCapacity;
        this.lastDigitPlate = lastDigitPlate;
        this.state = new AvailableState();
        this.stateName = this.state.getStateName();

        for (String path : photoPaths) {
            Photo photo = new Photo("General", path);
            photo.setVehicle(this);
            this.photos.add(photo);
        }
    }

    @PostLoad
    @SuppressWarnings("unused")
    private void syncStateFromName() {
        if (stateName == null) {
            this.state = new AvailableState();
            this.stateName = this.state.getStateName();
            return;
        }

        this.state = switch (stateName) {
            case "IN_NEGOTIATION" -> new InNegotiationState();
            case "SOLD" -> new SoldState();
            default -> new AvailableState();
        };
    }

    @Override public void showDetail() {
        System.out.println("Brand: " + brand + " | Model: " + model +
                " | Type: " + type + " | Price: $" + price);
    }

    @Override public double getPrice() { return price; }
    @Override public String getBrand() { return brand; }
    @Override public int getModel() { return model; }
    @Override public VehicleType getType() { return type; }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getLastDigitPlate() { return lastDigitPlate; }

    @Override
    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>();
        for (Photo p : photos) paths.add(p.getFilePath());
        return paths;
    }

    @Override public Advisor getAdvisor() { return advisor; }
    @Override public void setAdvisor(Advisor advisor) { this.advisor = advisor; }
    @Override public List<MaintenanceRecord> getMaintenanceHistory() { return maintenanceHistory; }

    @Override
    public void addMaintenance(MaintenanceRecord record) {
        record.setVehicle(this);
        this.maintenanceHistory.add(record);
    }

    @Override public void setState(VehicleState state) {
        this.state = state;
        this.stateName = state.getStateName();
    }
    @Override public VehicleState getState() { return state; }
    @Override public void startNegotiation() { state.startNegotiation(this); }
    @Override public void confirmSale() { state.confirmSale(this); }
    @Override public void cancelNegotiation() { state.cancelNegotiation(this); }
    @Override public String getStateName() { return stateName; }

    public Long getId() { return id; }
    public void setPrice(double price) { this.price = price; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(int model) { this.model = model; }
    public void setType(VehicleType type) { this.type = type; }
    public void setPassengerCapacity(int passengerCapacity) { this.passengerCapacity = passengerCapacity; }
    public void setLastDigitPlate(int lastDigitPlate) { this.lastDigitPlate = lastDigitPlate; }
    public List<Photo> getPhotos() { return photos; }
    @Override
    public boolean isInsurable() { return insurable; }
    public void setInsurable(boolean insurable) { this.insurable = insurable; }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setMaintenanceHistory(List<MaintenanceRecord> maintenanceHistory) {
        this.maintenanceHistory = maintenanceHistory;
    }
}