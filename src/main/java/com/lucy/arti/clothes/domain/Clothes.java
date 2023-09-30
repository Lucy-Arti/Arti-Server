package com.lucy.arti.clothes.domain;

import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.like.model.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "clothes_name")
    private String name;

    private String preview;

    @Column(name = "detail_img")
    private String img;

    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designer;

    @OneToMany(mappedBy = "clothes")
    private final List<Like> likes = new ArrayList<>();

}
