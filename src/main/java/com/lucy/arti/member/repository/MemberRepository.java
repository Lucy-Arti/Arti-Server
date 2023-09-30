package com.lucy.arti.member.repository;

import com.lucy.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByKakaoId(Long kakaoId);

    Optional<Member> findByAccessToken(String accessToken);

}
