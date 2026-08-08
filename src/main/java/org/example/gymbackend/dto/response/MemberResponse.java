package org.example.gymbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gymbackend.entity.Member;
import org.example.gymbackend.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Status.MemberStatus status;
    private LocalDate joinDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponse fromEntity(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getDateOfBirth(),
                member.getStatus(),
                member.getJoinDate(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
