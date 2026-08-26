package org.example.gymbackend.mapper.implementation;

import org.example.gymbackend.dto.response.MembershipResponse;
import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.Status;
import org.example.gymbackend.mapper.MembershipMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MembershipMapperImpl implements MembershipMapper {

    @Override
    public MembershipResponse toResponse(Membership entity) {
        Status.MembershipStatus stored = entity.getStatus();
        Status.MembershipStatus effectiveStatus = stored;

        if (stored != Status.MembershipStatus.SUSPENDED && stored != Status.MembershipStatus.CANCELLED) {
            effectiveStatus = LocalDate.now().isAfter(entity.getEndDate())
                    ? Status.MembershipStatus.EXPIRED
                    : Status.MembershipStatus.ACTIVE;
        }

        return new MembershipResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getMember().getFirstName() + " " + entity.getMember().getLastName(),
                entity.getPlan().getId(),
                entity.getPlan().getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                effectiveStatus
        );
    }
}
