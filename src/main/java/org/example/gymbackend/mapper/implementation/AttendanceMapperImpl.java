package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.AttendanceResponse;
import org.example.gymbackend.entity.Attendance;
import org.example.gymbackend.mapper.AttendanceMapper;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapperImpl implements AttendanceMapper {

    @Override
    public AttendanceResponse toResponse(Attendance entity) {
        return new AttendanceResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getMember().getFirstName() + " " + entity.getMember().getLastName(),
                entity.getClassSession() != null ? entity.getClassSession().getId() : null,
                entity.getClassSession() != null ? entity.getClassSession().getClassName() : null,
                entity.getCheckInTime(),
                entity.getCheckOutTime(),
                entity.getDurationMinutes()
        );
    }
}
