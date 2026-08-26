package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.AttendanceResponse;
import org.example.gymbackend.dto.request.CheckInRequest;

public interface AttendanceService {

    AttendanceResponse checkIn(CheckInRequest request);
    AttendanceResponse checkOut(String memberId);
    List<AttendanceResponse> getHistoryForMember(String memberId);
}
