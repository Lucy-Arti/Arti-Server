package com.lucy.arti.clothes.domain;

import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.winner.domain.Winner;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Clothes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_id")
    private Long id;

    @Column(name = "clothes_name", length = 2000)
    private String name;

    private String preview;

    @Column(name = "detail_img", length = 2000)
    private String img;

    private Long likeCount;

    private long score;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designer;

    @OneToMany(mappedBy = "clothes")
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "clothes")
    private final List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "clothes")
    private final List<Winner> winners = new ArrayList<>();

    public void addScore(int score) {
        this.score += score;
    }

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() { this.likeCount -= 1; }
}
