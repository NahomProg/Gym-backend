package org.example.gymbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.example.gymbackend.entity.Status;
import java.time.*;

@Data
public class UpdateMemberRequest {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    private Status.MemberStatus status;
}
