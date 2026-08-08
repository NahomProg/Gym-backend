package org.example.gymbackend.repository;

import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Status.MemberStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Member> findByStatus(MemberStatus status);
    List<Member> findByJoinDateBetween(LocalDate startDate, LocalDate endDate);
    List<Member> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
}