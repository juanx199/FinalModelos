package com.mycompany.carmotor.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "fotografias")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private PhotoCategory area;// "Exterior", "Interior", "Engine"

    private String filePath; // ruta o nombre del archivo

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public Photo() {}

    public Photo(String  area, String filePath) {
        this.area = PhotoCategory.fromString(area);
        this.filePath = filePath;
    }

    // Mantiene el nombre original para no romper VehiculoReal
    public String getRuta() {
        return filePath;
    }

    public PhotoCategory getArea() { return area; }
    public void setArea(PhotoCategory area) { this.area = area; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public String toString() {
        return "Foto [" + area + "]: " + filePath;
    }
}