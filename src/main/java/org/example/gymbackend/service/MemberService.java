package org.example.gymbackend.service;

import java.util.List;
import org.example.gymbackend.dto.response.MemberResponse;
import org.example.gymbackend.dto.request.CreateMemberRequest;
import org.example.gymbackend.dto.request.UpdateMemberRequest;

public interface MemberService {

    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(String id);
    List<MemberResponse> getAllMembers();
    MemberResponse updateMember(String id, UpdateMemberRequest request);
}
