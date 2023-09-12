package com.example.healthcare.controller.request;

import com.example.healthcare.domain.enumType.MemberDisclosureStatusRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {
    private String userId;
    private String email;
    private String password;
    private String nickname;
    private MemberDisclosureStatusRole disclosureStatus;

    // 클라이언트에서 보낸 데이터의 유효성 검증 로직 추가
}

