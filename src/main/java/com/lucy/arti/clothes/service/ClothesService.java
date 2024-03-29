package com.lucy.arti.clothes.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.clothes.dto.ClothesCreateRequestDto;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.repository.DesignerRepository;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import com.lucy.arti.global.util.S3Manager;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// 페이지네이션
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ClothesService {

    private final ClothesRepository clothesRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final S3Manager s3Manager;
    private final DesignerRepository designerRepository;

    @Transactional
    public String createClothes(ClothesCreateRequestDto requestDto, MultipartFile preview,
        MultipartFile img) throws IOException {
        checkIsNull(requestDto);
        Designer designer = designerRepository.findByIdOrThrow(requestDto.designerId());

        Clothes clothes = clothesRepository.save(Clothes.builder()
            .name(requestDto.name())
            .type(Type.valueOf(requestDto.type()))
            .designer(designer)
            .preview(uploadClothesImages(preview))
            .img(uploadClothesImages(img))
            .likeCount(0L)
            .build());
        return clothes.getId().toString();
    }

    private void checkIsNull(ClothesCreateRequestDto requestDto) {
        if (requestDto.name() == null) {
            throw BusinessException.from(ErrorCode.CLOTHES_NO_NAME);
        }
        if (requestDto.type() == null) {
            throw BusinessException.from(ErrorCode.CLOTHES_NO_TYPE);
        }
    }

    private String uploadClothesImages(MultipartFile image) throws IOException {
        return s3Manager.upload(image, "arti-lookbook");
    }

    @Transactional
    public ClothesDetailResponseDto update(Long clothesId, ClothesCreateRequestDto requestDto,
        MultipartFile preview, MultipartFile img) throws IOException {
        Clothes clothes = clothesRepository.findByIdOrThrow(clothesId);
        findAndChange(clothes, requestDto, preview, img);

        Clothes updated = clothesRepository.save(clothes);
        return ClothesDetailResponseDto.of(updated, updated.getDesigner());
    }

    private void findAndChange(Clothes clothes, ClothesCreateRequestDto requestDto,
        MultipartFile preview, MultipartFile img)
        throws IOException {

        if (requestDto.name() != null) {
            clothes.updateName(requestDto.name());
        }

        if (requestDto.type() != null) {
            clothes.updateType(Type.valueOf(requestDto.type()));
        }

        if (requestDto.link() != null) {
            clothes.updateLink(requestDto.link());
        }

        if (requestDto.designerId() != null) {
            clothes.updateDesigner(designerRepository.findByIdOrThrow(requestDto.designerId()));
        }

        if (preview != null) {
            clothes.updatePreview(uploadClothesImages(preview));
        }

        if (img != null) {
            clothes.updateDetailImage(uploadClothesImages(img));
        }
    }

    public List<?> getAll() {
        return clothesRepository.findAll().stream()
            .map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

    public Page<ClothesDetailResponseDto> getTypeAll(Type type, int page) {
        Pageable pageable = PageRequest.of(page, 8); // page는 조회할 페이지 번호, size는 한 페이지에 보여줄 옷 개수

        Page<Clothes> clothesPage = clothesRepository.findAllByType(type, pageable);

        List<ClothesDetailResponseDto> clothesDetailResponseList = clothesPage.getContent().stream()
            .map(x -> ClothesDetailResponseDto.of(x, x.getDesigner()))
            .collect(Collectors.toList());

        return new PageImpl<>(clothesDetailResponseList, pageable, clothesPage.getTotalElements());
    }

    public ClothesDetailResponseDto getById(Long clothesId) {
        Clothes clothes = clothesRepository.findById(clothesId).get();
        return ClothesDetailResponseDto.of(clothes, clothes.getDesigner());
    }

    public List<?> searchClothes(String query) {
        return clothesRepository.searchClothes(query).stream()
            .map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
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

        if (isLiked) {
            Like deletedLike = likeRepository.findByMemberIdAndClothesId(member.getId(), clothesId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 좋아요 id를 찾을 수 없습니다."));
            clothes.minusLikeCount();
            likeRepository.delete(deletedLike);
        } else {
            Like like = new Like(member, clothes);
            likeRepository.save(like);
            clothes.addLikeCount();
            clothesRepository.save(clothes);
        }
    }

    public List<ClothesDetailResponseDto> getClothesByDesignerId(Long designerId) {
        return clothesRepository.findByDesignerId(designerId).stream()
            .map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

    public List<ClothesDetailResponseDto> sortClothes() {
        return clothesRepository.findAll(Sort.by(Sort.Direction.DESC, "score")).stream()
            .map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList();
    }

}