package org.example.gymbackend.repository;

import org.example.gymbackend.entity.ClassSession;
import org.example.gymbackend.entity.Status.ClassStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, String> {

    List<ClassSession> findByStatus(ClassStatus status);
    List<ClassSession> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ClassSession> findByStartTimeAfterAndStatus(LocalDateTime date, ClassStatus status);
}