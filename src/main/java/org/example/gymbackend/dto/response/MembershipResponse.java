package org.example.gymbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
