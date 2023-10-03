package com.lucy.arti.like.repository;

import com.lucy.arti.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
