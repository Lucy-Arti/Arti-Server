package com.lucy.arti.member.repository;

import com.lucy.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByKakaoId(Long kakaoId);

    Member findByAccessToken(String accessToken);

    Member findByUserName(String username);
}
