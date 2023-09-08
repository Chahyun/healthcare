package com.example.healthcare.domain.memeber;

import com.example.healthcare.controller.request.member.AuthenticationRequest;
import com.example.healthcare.controller.request.member.MemberRequest;
import com.example.healthcare.domain.enumType.member.MemberDisclosureStatusRole;
import com.example.healthcare.domain.enumType.member.MemberStatusRole;
import com.example.healthcare.domain.enumType.member.MemberTypeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String email;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberTypeRole memberType;

    @Enumerated(EnumType.STRING)
    private MemberStatusRole memberStatus;

    @Enumerated(EnumType.STRING)
    private MemberDisclosureStatusRole disclosureStatus;

    private LocalDateTime registerDt;
    private LocalDateTime updateDt;

    private Double height;
    private Double weight;
    private Double muscleMass;
    //이메일 유효성
    public static boolean isValidEmail(String email) {
        // 이메일 형식을 정규표현식으로 확인
        String emailRegex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        return email.matches(emailRegex);
    }


    // 비밀번호 유효성
    public static boolean isValidPassword(String password){
        return password.matches("^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberType.name()));
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Member createMember(AuthenticationRequest request, String encodePassword) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Member.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .password(encodePassword)
                .nickname(request.getNickname())
                .memberType(MemberTypeRole.USER)
                .registerDt(currentTime)
                .memberStatus(MemberStatusRole.ACTIVE)
                .disclosureStatus(MemberDisclosureStatusRole.PUBLIC)
                .height(0.0)
                .weight(0.0)
                .muscleMass(0.0)
                .build();
    }

    public static Member updateMember(Member member, MemberRequest request, String encodePassword) {
        LocalDateTime currentTime = LocalDateTime.now();
        member.setEmail(request.getEmail());
        member.setNickname(request.getNickname());
        member.setPassword(encodePassword);
        member.setUpdateDt(currentTime);
        member.setHeight(request.getHeight());
        member.setWeight(request.getWeight());
        member.setMuscleMass(request.getMuscleMass());
        return member;
    }

}
