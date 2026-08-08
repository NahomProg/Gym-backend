package org.example.gymbackend.repository;

import org.example.gymbackend.entity.Equipment;
import org.example.gymbackend.entity.Status.EquipmentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

    List<Equipment> findByStatus(EquipmentStatus status);
    List<Equipment> findByNameContainingIgnoreCase(String name);
}