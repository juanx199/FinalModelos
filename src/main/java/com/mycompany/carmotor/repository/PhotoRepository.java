package com.mycompany.carmotor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.carmotor.model.domain.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByVehicleId(Long vehicleId);

    List<Photo> findByAreaIgnoreCase(String area);
}