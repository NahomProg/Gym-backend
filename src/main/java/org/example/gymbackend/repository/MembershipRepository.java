package org.example.gymbackend.repository;

import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.Status.MembershipStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, String> {

    List<Membership> findByMemberId(String memberId);
    List<Membership> findByMemberIdAndStatus(String memberId, MembershipStatus status);
    List<Membership> findByStatus(MembershipStatus status);
    List<Membership> findByEndDateBefore(LocalDate date);
}