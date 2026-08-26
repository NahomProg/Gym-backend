package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.entity.MembershipPlan;
import org.example.gymbackend.mapper.MembershipPlanMapper;
import org.springframework.stereotype.Component;

@Component
public class MembershipPlanMapperImpl implements MembershipPlanMapper {

    @Override
    public MembershipPlanResponse toResponse(MembershipPlan entity) {
        return new MembershipPlanResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDurationDays(),
                entity.getPrice(),
                entity.getPerks()
        );
    }
}
