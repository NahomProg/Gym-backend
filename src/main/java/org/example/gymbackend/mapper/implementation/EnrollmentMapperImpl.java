package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.entity.ClassEnrollment;
import org.example.gymbackend.mapper.EnrollmentMapper;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public EnrollmentResponse toResponse(ClassEnrollment entity) {
        return toResponse(entity, null);
    }

    @Override
    public EnrollmentResponse toResponse(ClassEnrollment entity, Integer waitlistPosition) {
        return new EnrollmentResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getMember().getFirstName() + " " + entity.getMember().getLastName(),
                entity.getClassSession().getId(),
                entity.getClassSession().getClassName(),
                entity.isWaitlisted(),
                waitlistPosition,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
