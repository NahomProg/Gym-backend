package org.example.gymbackend.dto.request;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
public class UpdateMemberRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Status.MemberStatus status;
}
