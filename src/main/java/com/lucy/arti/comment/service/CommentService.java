package com.lucy.arti.comment.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.comment.domain.Answer;
import com.lucy.arti.comment.domain.Comment;
import com.lucy.arti.comment.dto.MemberDto;
import com.lucy.arti.comment.repository.AnswerRepository;
import com.lucy.arti.comment.repository.CommentRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.repository.PointHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.lucy.arti.comment.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    public Comment createComment(Clothes clothes,Member member, String content) {
        Comment comment = Comment.builder()
                .member(member)
                .content(content)
                .heart(0L)
                .answerCount(0L)
                .createdAt(LocalDateTime.now())
                .clothes(clothes)
                .build();
        Point point = pointRepository.findByMember(member);
        if(point.getCommentCount()<5){
            if(point.getCommentCount()==4){
                point.setComment(false);
            }
            if(point.getTotalComment()==0){
                point.addPoint(550L);
                PointHistory pointHistory = new PointHistory(point, "첫 댓글 달기", 500L);
                pointHistoryRepository.save(pointHistory);
                PointHistory pointHistory1 = new PointHistory(point, "댓글 달기", 50L);
                pointHistoryRepository.save(pointHistory1);
                point.addCommentCount();
                point.addTotalComment();
                pointRepository.save(point);
            }else{
                point.addPoint(50L);
                PointHistory pointHistory1 = new PointHistory(point, "댓글 달기", 50L);
                pointHistoryRepository.save(pointHistory1);
                point.addCommentCount();
                point.addTotalComment();
                pointRepository.save(point);
            }
        }
        return commentRepository.save(comment);
    }

    public String updateComment(Long commentId, Member member, String updatedContent) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if(existingComment.getMember()!=member){
            return "댓글 작성자만 댓글을 수정할 수 있습니다.";
        }else if(existingComment.getMember().equals(member)){
            existingComment.changeContent(updatedContent);
            commentRepository.save(existingComment);
            return "success";
        }else{
            return "에러가 발생했습니다.";
        }
    }


    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Answer createAnswer(Comment comment, String content, Member member) {
        Answer answer = Answer.builder()
                .content(content)
                .comment(comment)
                .member(member)
                .heart(0L)
                .createdAt(LocalDateTime.now())
                .build();
        return answerRepository.save(answer);
    }

    public String updateAnswer(Long answerId, Member member, String updatedContent) {
        Answer existingAnswer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if(existingAnswer.getMember()!=member){
            return "답글 작성자만 댓글을 수정할 수 있습니다.";
        }else if(existingAnswer.getMember().equals(member)){
            existingAnswer.changeContent(updatedContent);
            answerRepository.save(existingAnswer);
            return "success";
        }else{
            return "에러가 발생했습니다.";
        }
    }

    public List<Comment> getCommentsByClothesId(Long clothesId) {
        return commentRepository.findByClothesId(clothesId);
    }

    public List<Answer> getAnswersByCommentId(Long commentId) {

        return answerRepository.findByCommentId(commentId);
    }

    public List<CommentDto> getAllCommentsByClothesId(Long clothesId) {
        return commentRepository.findByClothesId(clothesId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());

        commentDto.setCreatedAt(comment.getCreatedAt());

        MemberDto memberDto = new MemberDto();
        memberDto.setUserName(comment.getMember().getUserName());
        memberDto.setProfile(comment.getMember().getProfile());
        commentDto.setMember(memberDto);


        return commentDto;
    }
}
