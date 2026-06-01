package com.mycompany.carmotor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mycompany.carmotor.model.domain.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
