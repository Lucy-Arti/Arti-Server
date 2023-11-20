package com.lucy.arti.designer.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerDetailResponseDto;
import com.lucy.arti.designer.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DesignerService {
    private final DesignerRepository designerRepository;
    public DesignerDetailResponseDto getById(Long designerId) {
        Designer designer = designerRepository.findById(designerId).get();
        return DesignerDetailResponseDto.of(designer);
    }
}
