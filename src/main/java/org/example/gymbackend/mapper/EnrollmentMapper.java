package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.entity.ClassEnrollment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", expression = "java(entity.getMember().getFirstName() + \" \" + entity.getMember().getLastName())")
    @Mapping(target = "classSessionId", source = "classSession.id")
    @Mapping(target = "className", source = "classSession.className")
    @Mapping(target = "waitlistPosition", ignore = true)
    EnrollmentResponse toResponse(ClassEnrollment entity);
    default EnrollmentResponse toResponse(ClassEnrollment entity, Integer waitlistPosition) {
        EnrollmentResponse response = toResponse(entity);
        response.setWaitlistPosition(waitlistPosition);
        return response;
    }
}
