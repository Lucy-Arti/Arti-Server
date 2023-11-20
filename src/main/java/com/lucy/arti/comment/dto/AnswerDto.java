package com.lucy.arti.comment.dto;

import com.lucy.arti.comment.domain.Answer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerDto {
    private Long id;
    private String content;
    private Long heart;
    private LocalDateTime createdAt;
    private MemberDto member;


    public static AnswerDto fromAnswer(Answer answer) {
        AnswerDto answerDTO = new AnswerDto();
        answerDTO.id = answer.getId();
        answerDTO.content = answer.getContent();
        answerDTO.heart = answer.getHeart();
        answerDTO.createdAt = answer.getCreatedAt();
        answerDTO.member=MemberDto.fromMember(answer.getMember());
        return answerDTO;
        }
}

