package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.ClassSessionResponse;
import org.example.gymbackend.dto.response.EnrollmentResponse;
import org.example.gymbackend.dto.request.CreateClassSessionRequest;
import org.example.gymbackend.dto.request.EnrollRequest;

public interface ClassSessionService {

    ClassSessionResponse createClass(CreateClassSessionRequest request);
    List<ClassSessionResponse> getAllClasses();
    ClassSessionResponse getClassById(String id);
    EnrollmentResponse enroll(String classSessionId, EnrollRequest request);
    EnrollmentResponse cancelEnrollment(String classSessionId, String memberId);
    List<EnrollmentResponse> getWaitlist(String classSessionId);
    List<EnrollmentResponse> getEnrollmentsForClass(String classSessionId);
}
