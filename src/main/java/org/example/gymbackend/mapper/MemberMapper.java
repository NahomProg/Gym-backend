package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.MemberResponse;
import org.example.gymbackend.entity.Member;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse toResponse(Member entity);
}
