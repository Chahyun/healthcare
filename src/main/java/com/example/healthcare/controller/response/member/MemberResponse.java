package com.example.healthcare.controller.response.member;

import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.domain.enumType.member.MemberDisclosureStatusRole;


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
    private Double height;
    private Double weight;
    private Double muscleMass;


    public static MemberResponse createMemberResponse(Member member){
        return MemberResponse.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .disclosureStatus(member.getDisclosureStatus())
                .registerDt(member.getRegisterDt())
                .updateDt(member.getUpdateDt())
                .height(member.getHeight())
                .weight(member.getWeight())
                .muscleMass(member.getMuscleMass())
                .build();

    }
}
