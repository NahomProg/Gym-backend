package org.example.gymbackend;

import com.fasterxml.jackson.databind.*;
import org.example.gymbackend.entity.Role;
import org.example.gymbackend.entity.User;
import org.example.gymbackend.repository.RoleRepository;
import org.example.gymbackend.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static String jwtToken;
    private static String memberId;
    private static String member2Id;
    private static String planId;
    private static String classId;
    private static String waitlistClassId;
    private static String equipmentId;
    private static String maintenanceRecordId;

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
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("MEMBER"));
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
    void plainMemberCannotCreateMember() throws Exception {
        mockMvc.perform(post("/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "firstName": "Should",
                              "lastName": "Fail",
                              "email": "should.fail@email.com",
                              "phoneNumber": "0000000000"
                            }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    void elevateUserToAdmin() {
        User user = userRepository.findByEmail("test1@gmail.com").orElseThrow();
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        user.setRole(adminRole);
        userRepository.save(user);
    }

    @Test
    @Order(5)
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
    @Order(6)
    void createSecondMember() throws Exception {
        String response = mockMvc.perform(post("/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "firstName": "Second",
                              "lastName": "Member",
                              "email": "test.member2@email.com",
                              "phoneNumber": "1234567891"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        member2Id = json.get("id").asText();
    }

    @Test
    @Order(7)
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
    @Order(8)
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
    @Order(9)
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
    @Order(10)
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
    @Order(11)
    void checkOutSucceeds() throws Exception {
        mockMvc.perform(post("/attendance/check-out/" + memberId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkOutTime").exists())
                .andExpect(jsonPath("$.durationMinutes").exists());
    }

    @Test
    @Order(12)
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
    @Order(13)
    void enrollMemberInClass() throws Exception {
        mockMvc.perform(post("/classes/" + classId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.waitlisted").value(false));
    }

    @Test
    @Order(14)
    void duplicateEnrollmentFails() throws Exception {
        mockMvc.perform(post("/classes/" + classId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(15)
    void createWaitlistTestClass() throws Exception {
        String response = mockMvc.perform(post("/classes")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "className": "Spin",
                              "startTime": "2026-09-02T09:00:00",
                              "endTime": "2026-09-02T10:00:00",
                              "maxCapacity": 1
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maxCapacity").value(1))
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        waitlistClassId = json.get("id").asText();
    }

    @Test
    @Order(16)
    void firstMemberTakesTheOnlySeat() throws Exception {
        mockMvc.perform(post("/classes/" + waitlistClassId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(memberId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.waitlisted").value(false));
    }

    @Test
    @Order(17)
    void secondMemberGetsWaitlistedInsteadOfRejected() throws Exception {
        mockMvc.perform(post("/classes/" + waitlistClassId + "/enroll")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "memberId": "%s" }
                        """.formatted(member2Id)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.waitlisted").value(true))
                .andExpect(jsonPath("$.waitlistPosition").value(1));
    }

    @Test
    @Order(18)
    void waitlistEndpointShowsSecondMemberInPositionOne() throws Exception {
        mockMvc.perform(get("/classes/" + waitlistClassId + "/waitlist")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].memberId").value(member2Id))
                .andExpect(jsonPath("$[0].waitlistPosition").value(1));
    }

    @Test
    @Order(19)
    void cancellingFirstMemberAutoPromotesSecond() throws Exception {
        mockMvc.perform(delete("/classes/" + waitlistClassId + "/enroll/" + memberId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        mockMvc.perform(get("/classes/" + waitlistClassId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentEnrollment").value(1));

        mockMvc.perform(get("/classes/" + waitlistClassId + "/waitlist")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/classes/" + waitlistClassId + "/enrollments")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.memberId == '" + member2Id + "')].waitlisted").value(false));
    }

    @Test
    @Order(20)
    void createEquipment() throws Exception {
        String response = mockMvc.perform(post("/equipment")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            {
                              "name": "Treadmill #1",
                              "purchaseDate": "2025-01-15T00:00:00"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        equipmentId = json.get("id").asText();
    }

    @Test
    @Order(21)
    void directStatusChangeIntoMaintenanceIsRejected() throws Exception {
        // Confirms the old generic status endpoint can no longer be used to
        // bypass the maintenance log - it must go through /maintenance.
        mockMvc.perform(patch("/equipment/" + equipmentId + "/status")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "status": "UNDER_MAINTENANCE" }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(22)
    void startMaintenanceRecordsReasonAndFlipsStatus() throws Exception {
        String response = mockMvc.perform(post("/equipment/" + equipmentId + "/maintenance")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "reason": "Belt is worn out and squeaking" }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.open").value(true))
                .andExpect(jsonPath("$.resolvedAt").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        maintenanceRecordId = json.get("id").asText();

        mockMvc.perform(get("/equipment/" + equipmentId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNDER_MAINTENANCE"));
    }

    @Test
    @Order(23)
    void startingMaintenanceAgainWhileOpenFails() throws Exception {
        mockMvc.perform(post("/equipment/" + equipmentId + "/maintenance")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "reason": "Duplicate report" }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(24)
    void directStatusChangeOutOfMaintenanceIsRejected() throws Exception {
        mockMvc.perform(patch("/equipment/" + equipmentId + "/status")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "status": "AVAILABLE" }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(25)
    void resolveMaintenanceRestoresAvailability() throws Exception {
        mockMvc.perform(patch("/equipment/" + equipmentId + "/maintenance/" + maintenanceRecordId + "/resolve")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "notes": "Replaced belt, tested working" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.open").value(false))
                .andExpect(jsonPath("$.resolvedAt").exists());

        mockMvc.perform(get("/equipment/" + equipmentId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    @Order(26)
    void resolvingAnAlreadyResolvedRecordFails() throws Exception {
        mockMvc.perform(patch("/equipment/" + equipmentId + "/maintenance/" + maintenanceRecordId + "/resolve")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content("""
                            { "notes": "Trying again" }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(27)
    void maintenanceHistoryShowsTheResolvedRecord() throws Exception {
        mockMvc.perform(get("/equipment/" + equipmentId + "/maintenance")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].reason").value("Belt is worn out and squeaking"))
                .andExpect(jsonPath("$[0].open").value(false));
    }
}