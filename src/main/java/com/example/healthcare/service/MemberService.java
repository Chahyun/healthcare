package com.example.healthcare.service;

import com.example.healthcare.config.JwtService;
import com.example.healthcare.controller.request.AuthenticationRequest;
import com.example.healthcare.controller.response.AuthenticationResponse;
import com.example.healthcare.controller.response.MemberResponse;
import com.example.healthcare.domain.Member;
import com.example.healthcare.domain.enumType.MemberDisclosureStatusRole;
import com.example.healthcare.domain.enumType.MemberStatusRole;
import com.example.healthcare.domain.enumType.MemberTypeRole;

import com.example.healthcare.exception.CustomExceptions;
import com.example.healthcare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final LocalDateTime currentTime = LocalDateTime.now();
    private final  AuthenticationManager authenticationManager;


    /**
     * 회원가입을 수행합니다.
     * 새로운 회원을 생성하고, 회원 정보를 검증한 후에 저장하며,
     * 생성된 회원에 대한 JWT 토큰을 반환합니다.
     *
     * @param request 회원 가입 정보
     * @return 생성된 회원의 JWT 토큰
     * @throws IllegalArgumentException 올바르지 않은 입력 데이터인 경우 발생
     */
    public AuthenticationResponse register(AuthenticationRequest request) {
        // 이메일과 비밀번호 유효성 검사
        if (!Member.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
        if (!Member.isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함한 8글자 이상이어야 합니다.");
        }

        // 중복된 아이디, 이메일, 닉네임 검사
        memberRepository.findByUserId(request.getUserId())
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
                });

        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
                });

        memberRepository.findByNickname(request.getNickname())
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
                });

        // 새로운 회원 생성
        Member member = Member.createMember(request, passwordEncoder.encode(request.getPassword()));

        // 회원 저장
        memberRepository.save(member);

        // JWT 토큰 생성 및 반환
        String jwtToken = jwtService.generateToken(member);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * 로그인을 수행합니다.
     * 사용자의 아이디와 비밀번호를 인증하고, 인증된 회원의 JWT 토큰을 반환합니다.
     *
     * @param request 로그인 요청 정보
     * @return 인증된 회원의 JWT 토큰
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticateMember(authenticationManager, request.getUserId(), request.getPassword());
        Member member = findMemberForUserId(request.getUserId());
        String jwtToken = jwtService.generateToken(member);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * 회원 탈퇴를 수행합니다.
     * 인증된 회원의 아이디와 비밀번호를 인증하고, 해당 회원의 정보를 삭제합니다.
     *
     * @param userId  탈퇴 대상 회원의 아이디
     * @param request 회원 탈퇴 요청 정보
     */
    public void delete(String userId, AuthenticationRequest request) {
        authenticateMember(authenticationManager, userId, request.getPassword());
        Member member = findMemberForUserId(userId);
        memberRepository.deleteById(member.getId());
    }

    /**
     * 회원의 공개/비공개 상태를 변경합니다.
     *
     * @param userId 변경 대상 회원의 아이디
     */
    public void changeDisclosure(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow();

        if (member.getDisclosureStatus() == MemberDisclosureStatusRole.PUBLIC) {
            member.setDisclosureStatus(MemberDisclosureStatusRole.PRIVATE);
        } else {
            member.setDisclosureStatus(MemberDisclosureStatusRole.PUBLIC);
        }
        memberRepository.save(member);
    }


    /**
     * 자신의 회원 정보를 조회합니다.
     *
     * @param userId  조회 대상 회원의 아이디
     * @param request 인증 정보
     * @return 회원 정보 응답
     */
    public MemberResponse getMemberInfoByUserId(String userId, AuthenticationRequest request) {
        Member member = findMemberForUserId(userId);
        authenticateMember(authenticationManager, userId, request.getPassword());
        return MemberResponse.createMemberResponse(member);
    }

    /**
     * 자신의 회원 정보를 업데이트합니다.
     *
     * @param userId  업데이트 대상 회원의 아이디
     * @param request 업데이트 요청 정보
     */
    public void update(String userId, AuthenticationRequest request) {
        log.info(userId);
        if(!Member.isValidEmail(request.getEmail())){
            throw new IllegalArgumentException("옳바른 이메일 형식이 아닙니다.");
        }
        if(!Member.isValidPassword(request.getPassword())){
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함한 8글자 이상이어야 합니다.");
        }

        Member member = memberRepository.findByUserId(userId).orElseThrow();
        if(!member.getEmail().equals(request.getEmail())){
            memberRepository.findByEmail(request.getEmail())
                    .ifPresent(findEmail -> {
                        throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
                    });
        }

        if(!member.getNickname().equals(request.getNickname())){
            memberRepository.findByNickname(request.getNickname())
                    .ifPresent(findNickname -> {
                        throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
                    });
        }

        memberRepository.save(Member.updateMember(member, request, passwordEncoder.encode(request.getPassword())));
    }

    /**
     * 회원 인증을 수행합니다.
     *
     * @param authenticationManager 스프링 인증 매니저
     * @param userId                인증할 회원의 아이디
     * @param password              인증할 회원의 비밀번호
     */
    public static void authenticateMember(AuthenticationManager authenticationManager,
                                          String userId,
                                          String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, password)
        );
    }

    /**
     * 아이디로 회원을 조회합니다.
     *
     * @param userId 조회할 회원의 아이디
     * @return 조회된 회원 정보
     * @throws CustomExceptions.MemberNotFoundException 회원을 찾을 수 없을 경우 예외 발생
     */
    public Member findMemberForUserId(String userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomExceptions.MemberNotFoundException("회원 정보가 없습니다."));
    }

    /**
     * 아이디넘버로 회원을 조회합니다.
     *
     * @param userId 조회할 회원의 아이디
     * @return 조회된 회원 정보
     * @throws CustomExceptions.MemberNotFoundException 회원을 찾을 수 없을 경우 예외 발생
     */
    public Member findMemberForId(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.MemberNotFoundException("회원 정보가 없습니다."));
    }
}
