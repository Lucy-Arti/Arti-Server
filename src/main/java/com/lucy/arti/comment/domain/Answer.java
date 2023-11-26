package com.lucy.arti.comment.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.comment.controller.CommentController;
import com.lucy.arti.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany
    @JoinTable(
            name = "answers_likes",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> memberList = new ArrayList<>();

    private String content;

    private Long heart;

    private LocalDateTime createdAt;

    public void changeContent(String updatedContent){
        this.content = updatedContent;
    }


    @Builder
    public Answer(Comment comment, Member member, String content, Long heart, LocalDateTime createdAt) {
        this.member = member;
        this.content = content;
        this.heart = heart;
        this.createdAt = createdAt;
        this.comment = comment;
    }

    public boolean addLike(Member member) {
        if (memberList.contains(member)) {
            return false;
        }else{
            memberList.add(member);
            this.heart += 1;
            return true;
        }
    }

    public boolean isUserInMemberList(Member loggedInUser) {
        return memberList.contains(loggedInUser);
    }
}
