package com.example.healthcare.service;

import com.example.healthcare.config.JwtService;
import com.example.healthcare.controller.request.RegisterRequest;
import com.example.healthcare.controller.response.RegisterResponse;
import com.example.healthcare.domain.Member;
import com.example.healthcare.domain.enumType.MemberDisclosureStatusRole;
import com.example.healthcare.domain.enumType.MemberStatusRole;
import com.example.healthcare.domain.enumType.MemberTypeRole;
import com.example.healthcare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final LocalDateTime currentTime = LocalDateTime.now();

    /**
     * 회원가입
     * 일반 회원가입
     * Role이 USER로 들어감
     */

    public RegisterResponse register(RegisterRequest request){
        if(!Member.isValidEmail(request.getEmail())){
            throw new IllegalArgumentException("옳바른 이메일 형식이 아닙니다.");
        }
        if(!Member.isValidPassword(request.getPassword())){
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함한 8글자 이상이어야 합니다.");
        }
        Optional<Member> optionalUsername = memberRepository.findByUsername(request.getUsername());
        if(optionalUsername.isPresent()){
            throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
        }
        Optional<Member> optionalEmail = memberRepository.findByEmail(request.getEmail());
        if(optionalEmail.isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
        }
        Optional<Member> optionalNickname = memberRepository.findByNickname(request.getNickname());
        if(optionalNickname.isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .memberType(MemberTypeRole.USER)
                .registerDt(currentTime)
                .memberStatus(MemberStatusRole.ACTIVE)
                .disclosureStatus(MemberDisclosureStatusRole.PUBLIC)
                .build();

        memberRepository.save(member);

        String jwtToken = jwtService.generateToken(member);
        return RegisterResponse.builder()
                .token(jwtToken)
                .build();
    }
}
