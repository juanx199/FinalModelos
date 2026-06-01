package com.mycompany.carmotor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mycompany.carmotor.model.domain.TestDriveBooking;

@Repository
public interface TestDriveBookingRepository extends JpaRepository<TestDriveBooking, Long> {
    boolean existsByBranchNameAndSlotId(String branchName, String slotId);
    boolean existsByBranchNameAndSlotIdAndIdNot(String branchName, String slotId, Long id);
    List<TestDriveBooking> findByVehicleIdOrderByBookingTimeDesc(Long vehicleId);
}
