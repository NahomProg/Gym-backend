package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.ClassSessionResponse;
import org.example.gymbackend.entity.ClassSession;
import org.example.gymbackend.mapper.ClassSessionMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassSessionMapperImpl implements ClassSessionMapper {

    @Override
    public ClassSessionResponse toResponse(ClassSession entity) {
        return new ClassSessionResponse(
                entity.getId(),
                entity.getClassName(),
                entity.getClassDescription(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getMaxCapacity(),
                entity.getCurrentEnrollment(),
                entity.getStatus()
        );
    }
}
