package com.lucy.arti.comment.repository;

import com.lucy.arti.comment.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByCommentId(Long commentId);
}
