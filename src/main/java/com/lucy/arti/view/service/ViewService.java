package com.lucy.arti.view.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.view.domain.View;
import com.lucy.arti.view.repository.ViewRepository;
import com.lucy.arti.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ViewService {
    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ViewRepository viewRepository;
    @Transactional
    public ClothesDetailResponseDto getByIdWithToken(final Authentication authentication, Long clothesId) {
        long userKakaoId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userKakaoId).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();

        View viewObject = viewRepository.findByClothesId(clothesId);
        if (viewObject == null) {
            // 본적이 없는 옷일 때 저장
            View view = new View(member, clothes);
            log.info(view.getClothes().getName());
            viewRepository.save(view);
        } else {
            log.info(viewObject.getClothes().getName());
        }
        return ClothesDetailResponseDto.of(clothes, clothes.getDesigner());
    }

    public List<ClothesDetailResponseDto> getAllRecent(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).get();
        List<View> allViews = viewRepository.findAllByMemberId(member.getId());
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (View view : allViews) {
            allClothes.add(ClothesDetailResponseDto.of(view.getClothes(), view.getClothes().getDesigner()));
        }
        return allClothes;
    }
}
