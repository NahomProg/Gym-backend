package org.example.gymbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.*;

@Entity
@Table(name = "membership_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    private BigDecimal price;

    private String perks;
}