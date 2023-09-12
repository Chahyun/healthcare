package com.example.healthcare.controller.response;

import com.example.healthcare.controller.request.AuthenticationRequest;
import com.example.healthcare.domain.Member;
import com.example.healthcare.domain.enumType.MemberDisclosureStatusRole;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private String userId;
    private String email;
    private String nickname;
    private MemberDisclosureStatusRole disclosureStatus;
    private LocalDateTime registerDt;
    private LocalDateTime updateDt;


    public static MemberResponse createMemberResponse(Member member){
        return MemberResponse.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .disclosureStatus(member.getDisclosureStatus())
                .registerDt(member.getRegisterDt())
                .updateDt(member.getUpdateDt())
                .build();

    }
}
