package com.lucy.arti.member.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final ClothesRepository clothesRepository;
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
        Member member = memberRepository.findByKakaoId(userId).get();
        List<Like> allLikes = likeRepository.findAllByMemberId(member.getId());
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (Like likes : allLikes) {
            allClothes.add(ClothesDetailResponseDto.of(likes.getClothes(), likes.getClothes().getDesigner()));
        }
        return allClothes;
    }

}
