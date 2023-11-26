package com.lucy.arti.comment.dto;

import com.lucy.arti.comment.domain.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private Long heart;
    private Long answerCount;
    private LocalDateTime createdAt;
    private MemberDto member;
    private List<AnswerDto> answers;
    private boolean like;

    public static CommentDto fromComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        commentDto.setHeart(comment.getHeart());
        commentDto.setAnswerCount(comment.getAnswerCount());
        commentDto.setId(comment.getId());
        commentDto.setMember(MemberDto.fromMember(comment.getMember()));
        commentDto.setCreatedAt(comment.getCreatedAt());
        return commentDto;
    }

}