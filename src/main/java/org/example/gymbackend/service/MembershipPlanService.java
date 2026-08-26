package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.MembershipPlanResponse;
import org.example.gymbackend.dto.request.CreateMembershipPlanRequest;

public interface MembershipPlanService {

    MembershipPlanResponse createPlan(CreateMembershipPlanRequest request);
    List<MembershipPlanResponse> getAllPlans();
}
