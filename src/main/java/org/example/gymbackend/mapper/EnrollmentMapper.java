package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.entity.ClassEnrollment;

public interface EnrollmentMapper extends EntityMapper<ClassEnrollment, EnrollmentResponse> {
    EnrollmentResponse toResponse(ClassEnrollment entity, Integer waitlistPosition);
}
