package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.MembershipResponse;
import org.example.gymbackend.dto.request.AssignMembershipRequest;

public interface MembershipService {

    MembershipResponse assignMembership(AssignMembershipRequest request);
    MembershipResponse getMembershipById(String id);
    List<MembershipResponse> getMembershipsByMember(String memberId);
}
