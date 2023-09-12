package com.lucy.arti.member.model;

import com.amazonaws.services.s3.AmazonS3;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 기본 생성자를 생성해주는 역할
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=15, nullable = false)
    private String username;
    @Column(length = 50, nullable = false)
    private String email;

    @Builder // 빌더 패턴 생성
    public Member(Long id, String username, String email, AmazonS3 amazonS3) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
