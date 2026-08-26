package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.MemberResponse;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.mapper.MemberMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberResponse toResponse(Member entity) {
        return new MemberResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getDateOfBirth(),
                entity.getStatus(),
                entity.getJoinDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
