package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.ClassSessionResponse;
import org.example.gymbackend.entity.ClassSession;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClassSessionMapper {

    ClassSessionResponse toResponse(ClassSession entity);
}
