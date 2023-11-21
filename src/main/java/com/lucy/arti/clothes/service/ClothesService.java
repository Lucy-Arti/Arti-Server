package com.lucy.arti.clothes.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 좋아요 id를 찾을 수 없습니다."));
            clothes.minusLikeCount();
            likeRepository.delete(deletedLike);
        }
        else {
            Like like = new Like(member, clothes);
            likeRepository.save(like);
            clothes.addLikeCount();
            clothesRepository.save(clothes);
        }
    }
    public List<ClothesDetailResponseDto> getClothesByDesignerId(Long designerId) {
        return clothesRepository.findByDesignerId(designerId).stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

    public List<ClothesDetailResponseDto> sortClothes() {
        return clothesRepository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

//    @Transactional
//    public ClothesDetailResponseDto getByIdWithToken(final Authentication authentication, Long clothesId) {
//        System.out.println("들어옴");
//        long userKakaoId = Long.parseLong(authentication.getName());
//        Member member = memberRepository.findByKakaoId(userKakaoId).get();
//        Clothes clothes = clothesRepository.findById(clothesId).get();
//
//        log.info((member.getUserName()));
//
//        View viewObject = viewRepository.findByClothesId(clothesId);
//        log.info(viewObject.getClothes().getName());
//
//        // save 하는 방법
//        if(!viewObject.getClothes().equals(clothes)) { // 본적이 없는 옷일 떄 저장
//            View view = new View(member, clothes);
//            viewRepository.save(view);
//            log.info("view에 저장 됨");
//        }
//
//        return ClothesDetailResponseDto.of(clothes, clothes.getDesigner());
//    }
}
