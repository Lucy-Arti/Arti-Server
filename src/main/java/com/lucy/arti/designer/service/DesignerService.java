package com.lucy.arti.designer.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerDetailResponseDto;
import com.lucy.arti.designer.dto.DesignerPostDto;
import com.lucy.arti.designer.repository.DesignerRepository;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import com.lucy.arti.global.util.S3Manager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DesignerService {

    private final DesignerRepository designerRepository;

    private final S3Manager s3;

    @Transactional
    public String create(DesignerPostDto postDto, MultipartFile designerImage) throws IOException {
        validateDesigner(postDto);

        String designerProfile = "default";

        if (designerImage != null && !designerImage.isEmpty()) {
            designerProfile = uploadDesignerProfile(designerImage);
        }

        Designer designer = designerRepository.save(
            Designer.builder()
                .userName(postDto.getUserName())
                .introduce(postDto.getIntroduce())
                .instagram(postDto.getInstagram())
                .designerProfile(designerProfile)
                .build()
        );
        return designer.getId().toString();
    }

    @Transactional
    public DesignerDetailResponseDto update(Long designerId, DesignerPostDto postDto, MultipartFile designerImage)
        throws IOException {
        Designer designer = designerRepository.findByIdOrThrow(designerId);

        findAndChange(designer, postDto, designerImage);

        Designer updated = designerRepository.save(designer);

        return DesignerDetailResponseDto.of(updated);
    }

    private void findAndChange(Designer designer, DesignerPostDto postDto, MultipartFile designerImage)
        throws IOException {
        if (postDto.getUserName() != null) {
            designer.updateUserName(postDto.getUserName());
        }

        if (postDto.getIntroduce() != null) {
            designer.updateIntroduce(postDto.getIntroduce());
        }

        if (postDto.getInstagram() != null) {
            designer.updateInstagram(postDto.getInstagram());
        }

        if (designerImage != null) {
            designer.updateDesignerProfile(uploadDesignerProfile(designerImage));
        }
    }

    private String uploadDesignerProfile(MultipartFile designerImage) throws IOException {
        return s3.upload(designerImage, "designer");
    }

    private void validateDesigner(DesignerPostDto postDto) {
        if (postDto.getUserName() == null) {
            throw BusinessException.from(ErrorCode.DESIGNER_NO_NAME);
        }
    }

    public DesignerDetailResponseDto getById(Long designerId) {
        Designer designer = designerRepository.findByIdOrThrow(designerId);
        return DesignerDetailResponseDto.of(designer);
    }

    public List<DesignerDetailResponseDto> getAll() {
        return designerRepository.findAll().stream()
            .map(DesignerDetailResponseDto::of).toList();
    }

}
