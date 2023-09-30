package com.lucy.arti.like.model;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.member.model.Member;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "likes") // like가 MySQL의 예약어로 등록이 되어 있어 테이블 이름을 likesfh tjfwjdgkduTek.
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;
}
