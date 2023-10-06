package com.lucy.arti.view.repository;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.view.domain.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
public interface ViewRepository extends JpaRepository<View, Long> {
//    View findByClothesId(Long clothesId);

    View findByClothesAndMember(Clothes clothes, Member member);

    List<View> findAllByMemberId(Long memberId);
}
