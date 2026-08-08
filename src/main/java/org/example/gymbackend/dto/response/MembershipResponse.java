package org.example.gymbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gymbackend.entity.Membership;
import org.example.gymbackend.entity.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponse {
    private String id;
    private String memberId;
    private String memberName;
    private String planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status.MembershipStatus status;

    /**
     * Per ADR-002 in the architecture doc: expiry is computed on read, not
     * maintained by a background job. The stored "status" column is only
     * authoritative for SUSPENDED/CANCELLED (an admin decision); for the
     * normal case we derive ACTIVE vs EXPIRED live from endDate vs today.
     */
    public static MembershipResponse fromEntity(Membership membership) {
        Status.MembershipStatus effectiveStatus = computeEffectiveStatus(membership);

        return new MembershipResponse(
                membership.getId(),
                membership.getMember().getId(),
                membership.getMember().getFirstName() + " " + membership.getMember().getLastName(),
                membership.getPlan().getId(),
                membership.getPlan().getName(),
                membership.getStartDate(),
                membership.getEndDate(),
                effectiveStatus
        );
    }

    private static Status.MembershipStatus computeEffectiveStatus(Membership membership) {
        Status.MembershipStatus stored = membership.getStatus();

        if (stored == Status.MembershipStatus.SUSPENDED || stored == Status.MembershipStatus.CANCELLED) {
            return stored;
        }
        if (LocalDate.now().isAfter(membership.getEndDate())) {
            return Status.MembershipStatus.EXPIRED;
        }
        return Status.MembershipStatus.ACTIVE;
    }
}
