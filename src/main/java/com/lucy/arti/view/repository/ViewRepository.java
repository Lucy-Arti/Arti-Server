package com.lucy.arti.view.repository;

import com.lucy.arti.view.domain.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<View, Long> {
    View findByClothesId(Long clothesId);

    List<View> findAllByMemberId(Long memberId);
}
