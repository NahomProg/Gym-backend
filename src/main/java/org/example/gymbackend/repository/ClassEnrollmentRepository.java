package org.example.gymbackend.repository;

import org.example.gymbackend.entity.ClassEnrollment;
import org.example.gymbackend.entity.Status.EnrollmentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, String> {

    List<ClassEnrollment> findByMemberId(String memberId);
    List<ClassEnrollment> findByClassSessionId(String classSessionId);
    List<ClassEnrollment> findByClassSessionIdAndStatus(String classSessionId, EnrollmentStatus status);
    boolean existsByMemberIdAndClassSessionIdAndStatus(String memberId, String classSessionId, EnrollmentStatus status);
    long countByClassSessionIdAndStatus(String classSessionId, EnrollmentStatus status);

    Optional<ClassEnrollment> findByMemberIdAndClassSessionIdAndStatus(String memberId, String classSessionId, EnrollmentStatus status);
    List<ClassEnrollment> findByClassSessionIdAndWaitlistedTrueAndStatusOrderByCreatedAtAsc(String classSessionId, EnrollmentStatus status);
    Optional<ClassEnrollment> findFirstByClassSessionIdAndWaitlistedTrueAndStatusOrderByCreatedAtAsc(String classSessionId, EnrollmentStatus status);
}