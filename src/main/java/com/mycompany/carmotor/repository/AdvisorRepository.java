package com.mycompany.carmotor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.carmotor.model.domain.Advisor;

@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    List<Advisor> findByNombreIgnoreCase(String nombre);
}