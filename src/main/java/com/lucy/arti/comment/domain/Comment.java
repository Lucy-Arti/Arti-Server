package com.lucy.arti.comment.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import com.lucy.arti.member.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    private String content;

    private Long heart;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> memberList = new ArrayList<>();

    private Long answerCount;

    private LocalDateTime createdAt;

    @Builder
    public Comment(Clothes clothes,Member member, String content, Long heart, Long answerCount, LocalDateTime createdAt) {
        this.member = member;
        this.content = content;
        this.heart = heart;
        this.answerCount = answerCount;
        this.createdAt = createdAt;
        this.clothes = clothes;
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

    public void addAnswerCount(){this.answerCount = this.answerCount+1;}

    public void changeContent(String updatedContent){
        this.content = updatedContent;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        this.addAnswerCount();
    }


}
