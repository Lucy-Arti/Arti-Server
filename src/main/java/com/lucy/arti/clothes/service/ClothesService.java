package com.lucy.arti.clothes.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public List<?> getAll(){
        return clothesRepository.findAll().stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

    public ClothesDetailResponseDto getById(Long clothesId) {
        Clothes clothes = clothesRepository.findById(clothesId).get();
        return ClothesDetailResponseDto.of(clothes, clothes.getDesigner());
    }

    public List<?> searchClothes(String query) {
        return clothesRepository.searchClothes(query).stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

    @Transactional
    public boolean isLiked(final Authentication authentication, Long clothesId) {
        long userKakaoId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userKakaoId).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();

        boolean isLiked = likeRepository.existsByMemberAndClothes(member, clothes);
        return isLiked;
    }

    @Transactional
    public void like(final Authentication authentication, Long clothesId) {
        long userKakaoId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userKakaoId).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();
        boolean isLiked = isLiked(authentication, clothesId);
        if(isLiked) {
            Like deletedLike = likeRepository.findByMemberIdAndClothesId(member.getId(), clothesId)
                    .orElseThrow();
            likeRepository.delete(deletedLike);
        }
        else {
            Like like = new Like(member, clothes);
            likeRepository.save(like);
            clothes.addLikeCount();
            clothesRepository.save(clothes);
        }
    }



//    public List<Clothes> getClothesByDesignerId(final Authentication authentication, Long clothesId) {
//        long userKakaoId = Long.parseLong(authentication.getName());
//        List<Clothes> clothesById = clothesRepository.findByClothesID(clothesId);
//        return clothesById;
//    }
}
