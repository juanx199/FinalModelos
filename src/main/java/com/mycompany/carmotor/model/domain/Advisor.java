package com.mycompany.carmotor.model.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asesores")
public class Advisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String fotoAsesor;
    private String datosContacto;
    
    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Advisor() {}

    public Advisor(String nombre, String fotoAsesor, String datosContacto) {
        this.nombre = nombre;
        this.fotoAsesor = fotoAsesor;
        this.datosContacto = datosContacto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFotoAsesor() { return fotoAsesor; }
    public void setFotoAsesor(String fotoAsesor) { this.fotoAsesor = fotoAsesor; }
    public String getDatosContacto() { return datosContacto; }
    public void setDatosContacto(String datosContacto) { this.datosContacto = datosContacto; }
    public List<Vehicle> getVehiculos() { return vehicles; }

    // Aliases inglés
    public String getName() { return getNombre(); } 
    public void setName(String name) { setNombre(name); }
    public String getPhoto() { return getFotoAsesor(); }
    public void setPhoto(String photo) { setFotoAsesor(photo); }
    public String getContactInfo() { return getDatosContacto(); }
    public void setContactInfo(String contactInfo) { setDatosContacto(contactInfo); }

    @Override
    public String toString() {
        return "Asesor: " + nombre + " | Contacto: " + datosContacto;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}