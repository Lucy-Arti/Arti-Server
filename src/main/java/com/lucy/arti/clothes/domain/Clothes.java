package com.lucy.arti.clothes.domain;

import com.lucy.arti.comment.domain.Comment;
import com.lucy.arti.global.config.BaseTimeEntity;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.vote.domain.Vote;
import lombok.Builder;
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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "link")
    private String link;

    @OneToMany(mappedBy = "clothes")
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "clothes")
    private final List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "clothes")
    private final List<Comment> comments = new ArrayList<>();

    public void addScore(int score) {
        this.score += score;
    }

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() { this.likeCount -= 1; }

    @Builder
    public Clothes(String name, String preview, String img, Type type, String link, Designer designer, Long likeCount) {
        this.name = name;
        this.preview = preview;
        this.img = img;
        this.type = type;
        this.link = link;
        this.designer = designer;
        this.likeCount = likeCount;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateType(Type type) {
        this.type = type;
    }

    public void updateLink(String link) {
        this.link = link;
    }

    public void updatePreview(String preview) {
        this.preview = preview;
    }

    public void updateDetailImage(String img) {
        this.img = img;
    }

    public void updateDesigner(Designer designer) {
        this.designer = designer;
    }

    public int getCommentCountByClothes(Long clothesId) {
        return this.comments.size();
    }

}
