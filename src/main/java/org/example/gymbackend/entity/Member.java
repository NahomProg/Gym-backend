package org.example.gymbackend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status.MemberStatus status;

    @Column(nullable = false)
    private LocalDate joinDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<ClassEnrollment> classEnrollments = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}