package com.lucy.arti.clothes.domain;

import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.designer.domain.Designer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Clothes extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
