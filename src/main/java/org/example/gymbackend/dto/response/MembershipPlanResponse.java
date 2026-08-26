package org.example.gymbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlanResponse {
    private String id;
    private String name;
    private String description;
    private Integer durationDays;
    private BigDecimal price;
    private String perks;
}
