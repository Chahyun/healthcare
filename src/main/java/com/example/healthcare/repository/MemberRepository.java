package com.example.healthcare.repository;

import com.example.healthcare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String username);
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

}
