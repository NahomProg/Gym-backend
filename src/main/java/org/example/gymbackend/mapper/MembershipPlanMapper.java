package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.entity.MembershipPlan;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MembershipPlanMapper {

    MembershipPlanResponse toResponse(MembershipPlan entity);
}
