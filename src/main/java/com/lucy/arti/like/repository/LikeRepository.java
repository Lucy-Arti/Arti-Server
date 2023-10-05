package com.lucy.arti.like.repository;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    boolean existsByMemberAndClothes(Member member, Clothes clothes);

    Optional<Like> findByMemberIdAndClothesId(Long memberId, Long clothesId);
}
