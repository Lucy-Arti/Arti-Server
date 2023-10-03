package com.lucy.arti.vote.domain;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    private int score;

    public Vote(Member member, Clothes clothes, int score) {
        this.member = member;
        this.clothes = clothes;
        this.score = score;
    }
}
