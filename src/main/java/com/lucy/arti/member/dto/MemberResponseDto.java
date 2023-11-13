package com.lucy.arti.member.dto;

import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.dto.LikeDto;
import com.lucy.arti.member.domain.UserRole;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.dto.VoteDto;
import com.lucy.arti.winner.domain.Winner;
import com.lucy.arti.winner.dto.WinnerDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Data
public class MemberResponseDto {
    private Long id;
    private String userName;
    private String accessToken;
    private UserRole userRole;
    private String email;
    private String profile;
    private List<LikeDto> likes;
    private List<VoteDto> votes;
    private List<WinnerDto> winners;

    public MemberResponseDto(Long id, String userName, String email, String profile,
                     List<Like> likes, List<Vote> votes, List<Winner> winners, String accessToken, UserRole userRole) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.profile = profile;
        this.accessToken = accessToken;
        this.userRole = userRole;

        if (likes != null) {
            this.likes = likes.stream()
                    .map(like -> new LikeDto(like.getClothes().getId()))
                    .collect(Collectors.toList());
        }

        // votes 리스트를 VoteDto 리스트로 변환하여 할당
        if (votes != null) {
            this.votes = votes.stream()
                    .map(vote -> new VoteDto(vote.getClothes().getId()))
                    .collect(Collectors.toList());
        }

        // winners 리스트를 WinnerDto 리스트로 변환하여 할당
        if (winners != null) {
            this.winners = winners.stream()
                    .map(winner -> new WinnerDto(winner.getId()))
                    .collect(Collectors.toList());
        }
    }

}

