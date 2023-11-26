package com.lucy.arti.member.dto;

import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.dto.LikeDto;
import com.lucy.arti.member.domain.UserRole;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.dto.VoteDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String nickname;
    private String accessToken;
    private UserRole userRole;
    private String email;
    private String profile;
    private List<LikeDto> likes;
    private List<VoteDto> votes;

    public MemberResponseDto(Long id, String userName, String email, String profile,
        List<Like> likes, List<Vote> votes, String accessToken,
        UserRole userRole, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
        this.accessToken = accessToken;
        this.userRole = userRole;

        if (likes != null) {
            this.likes = likes.stream()
                .map(like -> new LikeDto(like.getClothes().getId()))
                .collect(Collectors.toList());
        }

        if (votes != null) {
            this.votes = votes.stream()
                .map(vote -> new VoteDto(vote.getClothes().getId()))
                .collect(Collectors.toList());
        }
    }

}

