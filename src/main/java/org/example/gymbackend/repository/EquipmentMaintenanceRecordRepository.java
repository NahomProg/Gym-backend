package org.example.gymbackend.repository;

import org.example.gymbackend.entity.EquipmentMaintenanceRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface EquipmentMaintenanceRecordRepository extends JpaRepository<EquipmentMaintenanceRecord, String> {

    List<EquipmentMaintenanceRecord> findByEquipmentIdOrderByReportedAtDesc(String equipmentId);
    Optional<EquipmentMaintenanceRecord> findByEquipmentIdAndResolvedAtIsNull(String equipmentId);
    boolean existsByEquipmentIdAndResolvedAtIsNull(String equipmentId);
    Optional<EquipmentMaintenanceRecord> findByIdAndEquipmentId(String id, String equipmentId);
}
