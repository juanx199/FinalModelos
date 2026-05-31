package com.mycompany.carmotor.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "entidades_bancarias")
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String logo;
    private String telefonoAsesor;

    public BankEntity() {}

    public BankEntity(String nombre, String logo, String telefonoAsesor) {
        this.nombre = nombre;
        this.logo = logo;
        this.telefonoAsesor = telefonoAsesor;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getTelefonoAsesor() { return telefonoAsesor; }
    public void setTelefonoAsesor(String telefonoAsesor) { this.telefonoAsesor = telefonoAsesor; }

    @Override
    public String toString() {
        return "Entidad Bancaria: " + nombre + " (Asesor Tel: " + telefonoAsesor + ")";
    }
}