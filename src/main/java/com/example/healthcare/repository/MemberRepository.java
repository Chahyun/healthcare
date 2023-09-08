package com.example.healthcare.repository;

import com.example.healthcare.domain.memeber.Member;
import com.example.healthcare.domain.enumType.member.MemberDisclosureStatusRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String username);
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    List<Member> findAllByDisclosureStatus(MemberDisclosureStatusRole memberDisclosureStatusRole);

}
