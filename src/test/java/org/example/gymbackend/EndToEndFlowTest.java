package org.example.gymbackend;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndToEndFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String jwtToken;
    private static String memberId;
    private static String planId;
    private static String classId;

    @Test
    @Order(1)
    void registerNewUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                              "email": "test1@gmail.com",
                              "password": "test1run"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Order(2)
    void loginReturnsJwt() throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("""
                            {
                              "email": "test1@gmail.com",
                              "password": "test1run"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        jwtToken = json.get("token").asText();
    }

    @Test
    @Order(3)
    void createMember() throws Exception {
        String response = mockMvc.perform(post("/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "firstName": "Test",
                              "lastName": "Member",
                              "email": "test.member@email.com",
                              "phoneNumber": "1234567890"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        memberId = json.get("id").asText();
    }

    @Test
    @Order(4)
    void createMembershipPlan() throws Exception {
        String response = mockMvc.perform(post("/membership-plans")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "name": "Monthly Plan",
                              "durationDays": 30,
                              "price": 1000
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        planId = json.get("id").asText();
    }

    @Test
    @Order(5)
    void assignMembershipToMember() throws Exception {
        mockMvc.perform(post("/memberships")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "memberId": "%s",
                              "planId": "%s"
                            }
                        """.formatted(memberId, planId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @Order(6)
    void checkInSucceedsWithActiveMembership() throws Exception {
        mockMvc.perform(post("/attendance/check-in")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.checkInTime").exists())
                .andExpect(jsonPath("$.checkOutTime").doesNotExist());
    }

    @Test
    @Order(7)
    void secondCheckInWithoutCheckoutFails() throws Exception {
        mockMvc.perform(post("/attendance/check-in")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(8)
    void checkOutSucceeds() throws Exception {
        mockMvc.perform(post("/attendance/check-out/" + memberId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkOutTime").exists())
                .andExpect(jsonPath("$.durationMinutes").exists());
    }

    @Test
    @Order(9)
    void createClass() throws Exception {
        String response = mockMvc.perform(post("/classes")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "className": "Swimming",
                              "startTime": "2026-09-01T09:00:00",
                              "endTime": "2026-09-01T10:00:00",
                              "maxCapacity": 5
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currentEnrollment").value(0))
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        classId = json.get("id").asText();
    }

    @Test
    @Order(10)
    void enrollMemberInClass() throws Exception {
        mockMvc.perform(post("/classes/" + classId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @Order(11)
    void duplicateEnrollmentFails() throws Exception {
        mockMvc.perform(post("/classes/" + classId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isConflict());
    }
}