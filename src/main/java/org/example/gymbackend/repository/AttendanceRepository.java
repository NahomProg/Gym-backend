package org.example.gymbackend.repository;

import org.example.gymbackend.entity.Attendance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {

      Optional<Attendance> findByMemberIdAndCheckOutTimeIsNull(String memberId);
      boolean existsByMemberIdAndCheckOutTimeIsNull(String memberId);
      List<Attendance> findByMemberIdOrderByCheckInTimeDesc(String memberId);
      List<Attendance> findByMemberIdAndCheckInTimeBetween(String memberId, LocalDateTime startDate, LocalDateTime endDate);
      List<Attendance> findByCheckInTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
      long countByCheckInTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
      long countByCheckOutTimeIsNull();
      List<Attendance> findByClassSessionId(String classSessionId);
}