package com.example.healthcare.controller.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {
    private String email;
    private String password;
    private String nickname;
    private Double height;
    private Double weight;
    private Double muscleMass;
}

