package com.example.healthcare.controller;

import com.example.healthcare.controller.request.member.AuthenticationRequest;
import com.example.healthcare.controller.request.member.MemberRequest;
import com.example.healthcare.controller.response.member.AuthenticationResponse;
import com.example.healthcare.controller.response.member.MemberResponse;
import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 일반 회원가입
     *
     * @param request 회원 가입 요청 정보
     * @return 회원 가입 결과 및 응답
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(memberService.register(request));
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    /**
     * 로그인 기능
     *
     * @param request 로그인 요청 정보
     * @return 로그인 결과 및 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(memberService.login(request));
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    /**
     * 로그아웃 기능
     *
     * @param request HTTP 요청
     * @return 로그아웃 결과 및 응답
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        request.getSession().invalidate(); // 세션 무효화
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


    /**
     * 회원 탈퇴 기능
     *
     * @param member  현재 인증된 회원 정보
     * @param request 탈퇴 요청 정보
     * @return 회원 탈퇴 결과 및 응답
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal Member member,
                                         @RequestBody AuthenticationRequest request) {
        memberService.delete(member.getUserId(), request);
        return ResponseEntity.ok("회원 삭제를 완료하였습니다");
    }

    /**
     * 계정 공개 - 비공개 전환 기능
     *
     * @param member 현재 인증된 회원 정보
     * @return 공개 전환 결과 및 응답
     */
    @PutMapping("/changeDisclosure")
    public ResponseEntity<String> changeDisclosure(@AuthenticationPrincipal Member member) {
        log.info(member.getUserId());
        memberService.changeDisclosure(member.getUserId());
        return ResponseEntity.ok("나의 공개 정보가 변경 되었습니다.");
    }

    /**
     * 내 정보 조회 기능
     *
     * @param member  현재 인증된 회원 정보
     * @param request 조회 요청 정보
     * @return 회원 정보 조회 결과 및 응답
     */
    @GetMapping("/memberInfo")
    public ResponseEntity<MemberResponse> memberInfo(@AuthenticationPrincipal Member member,
                                                     @RequestBody AuthenticationRequest request) {
        MemberResponse memberResponse = memberService.getMemberInfoByUserId(member.getUserId(), request);
        return ResponseEntity.ok(memberResponse);
    }

    /**
     * 계정 정보 변경 기능
     *
     * @param member  현재 인증된 회원 정보
     * @param request 변경 요청 정보
     * @return 계정 정보 변경 결과 및 응답
     */
    @PutMapping("/update")
    public ResponseEntity<String> update(@AuthenticationPrincipal Member member,
                                         @RequestBody MemberRequest request) {
        memberService.update(member.getUserId(), request);
        return ResponseEntity.ok("나의 정보가 변경 되었습니다.");
    }

}
