package com.mycompany.carmotor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.carmotor.model.domain.BankEntity;

@Repository
public interface BankEntityRepository extends JpaRepository<BankEntity, Long> {
}