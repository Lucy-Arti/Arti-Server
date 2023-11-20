package com.lucy.arti.member.domain;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.winner.domain.Winner;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.lucy.arti.global.config.BaseTimeEntity;
import com.lucy.arti.like.domain.Like;

import javax.persistence.*;
import java.time.LocalDateTime;
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
/**
    성별은 카카오싱크 등록 이후 불러올 수 있음
    @Enumerated(EnumType.STRING)
    private Gender gender;
 **/

    @OneToMany(mappedBy = "member")
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Winner> winners = new ArrayList<>();

    @Column(nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profile;

    private LocalDateTime lastVoted;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "custom_profile")
    private String customProfile;

    @Enumerated(EnumType.STRING)
    private UserRole authority;

    @Builder
    public Member(Long kakaoId, String username, String email, String profile, String nickname) {
        this.kakaoId = kakaoId;
        this.userName = username;
        this.email = email;
        this.profile = profile;
        this.authority = UserRole.ROLE_USER;
        this.lastVoted = LocalDateTime.of(1999, 1, 1, 0, 0);
        this.nickname = nickname;
    }
    public Set<UserRole> getAuthorities(){
        Set<UserRole> returnRole = new HashSet<>();
        returnRole.add(this.authority);
        return returnRole;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLastVoted() {
        this.lastVoted = LocalDateTime.now();
    }

    public void updateNickName(String nickName) {
        this.nickname = nickName;
    }

    public void updateProfile(String customProfile) {
        this.customProfile = customProfile;
    }
}
