package com.lucy.arti.member.repository;

import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import com.lucy.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByKakaoId(Long kakaoId);

    Member findByAccessToken(String accessToken);

    default Member findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
            () -> BusinessException.from(ErrorCode.MEMBER_NOT_FOUND));
    }

    default Member findByKakaoIdOrThrow(Long id) {
        return findByKakaoId(id).orElseThrow(
            () -> new IllegalArgumentException(("[Error] 해당 카카오 ID로 회원을 조회할 수 없습니다.")));
    }

    boolean existsByNickname(String nickname);
  
    Member findByUserName(String username);
}
