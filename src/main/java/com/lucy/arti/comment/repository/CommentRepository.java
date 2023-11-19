package com.lucy.arti.comment.repository;

import com.lucy.arti.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByClothesId(Long clothesId);
}
