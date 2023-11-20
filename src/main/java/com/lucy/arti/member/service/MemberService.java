package com.lucy.arti.member.service;

import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.dto.MemberUpdateResponseDto;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final LikeRepository likeRepository;
    public List<ClothesDetailResponseDto> victory(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).get();
        List<Vote> allVotes = voteRepository.findAllByMemberAndScore(member,4);
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (Vote vote : allVotes) {
            allClothes.add(ClothesDetailResponseDto.of(vote.getClothes(), vote.getClothes().getDesigner()));
        }
        return allClothes;
    }

    public List<ClothesDetailResponseDto> saveListShow(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 kakaoId를 가진 회원을 조회할 수 없습니다."));
        List<Like> allLikes = likeRepository.findAllByMemberId(member.getId());
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (Like likes : allLikes) {
            allClothes.add(ClothesDetailResponseDto.of(likes.getClothes(), likes.getClothes().getDesigner()));
        }
        return allClothes;
    }

    public MemberUpdateResponseDto updateNickname(final Authentication authentication) {
        long kakaoId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoIdOrThrow(kakaoId);
        String nickName = "tmp";
        member.updateNickName(nickName);
        Member updated = memberRepository.save(member);

        return new MemberUpdateResponseDto(
            updated.getKakaoId(),
            updated.getNickName()
        );
    }

}
