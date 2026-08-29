package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.AttendanceResponse;
import org.example.gymbackend.entity.Attendance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", expression = "java(entity.getMember().getFirstName() + \" \" + entity.getMember().getLastName())")
    @Mapping(target = "classSessionId", source = "classSession.id")
    @Mapping(target = "className", source = "classSession.className")
    AttendanceResponse toResponse(Attendance entity);
}
