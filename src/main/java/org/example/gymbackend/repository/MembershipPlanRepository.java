package org.example.gymbackend.repository;

import org.example.gymbackend.entity.MembershipPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, String> {

    Optional<MembershipPlan> findByName(String name);
}