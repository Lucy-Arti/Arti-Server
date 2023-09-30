package com.lucy.arti.member.model;
import com.amazonaws.services.s3.AmazonS3;
import com.lucy.arti.oauth.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.lucy.arti.config.BaseTimeEntity;
import com.lucy.arti.config.Gender;
import com.lucy.arti.like.model.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String accessToken;

    private String userName;

// 성별은 카카오에서 받아올 수 있는지 모르겠음
//    @Enumerated(EnumType.STRING)
//    private Gender gender;

    @OneToMany(mappedBy = "member")
    private final List<Like> likes = new ArrayList<>();

    private Long kakaoId;

    private String email;

    private String profile;

    //add for login
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="member_authority",
            joinColumns = {@JoinColumn(name="member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities = new HashSet<>();


    @Builder
    public Member(Long kakaoId, String username, String email, String profile) {
        this.kakaoId = kakaoId;
        this.userName = username;
        this.email = email;
        this.profile = profile;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
