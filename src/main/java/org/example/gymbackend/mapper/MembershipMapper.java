package org.example.gymbackend.mapper;

import org.example.gymbackend.dto.response.MembershipResponse;
import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.Status;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface MembershipMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", expression = "java(entity.getMember().getFirstName() + \" \" + entity.getMember().getLastName())")
    @Mapping(target = "planId", source = "plan.id")
    @Mapping(target = "planName", source = "plan.name")
    @Mapping(target = "status", expression = "java(effectiveStatus(entity))")
    MembershipResponse toResponse(Membership entity);

    default Status.MembershipStatus effectiveStatus(Membership entity) {
        Status.MembershipStatus stored = entity.getStatus();

        if (stored == Status.MembershipStatus.SUSPENDED || stored == Status.MembershipStatus.CANCELLED) {
            return stored;
        }
        if (LocalDate.now().isAfter(entity.getEndDate())) {
            return Status.MembershipStatus.EXPIRED;
        }
        return Status.MembershipStatus.ACTIVE;
    }
}
