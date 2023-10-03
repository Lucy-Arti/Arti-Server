package com.lucy.arti.clothes.dto;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.designer.domain.Designer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClothesDetailResponseDto {
    private Long clothesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String detailImg;
    private Long likeCount;
    private String clothesName;
    private String preview;
    private Long designerId;
    private String designerName;
    private long score;

    public static ClothesDetailResponseDto of(Clothes clothes, Designer designer) {
        return ClothesDetailResponseDto.builder()
                .clothesId(clothes.getId())
                .createdAt(clothes.getCreatedAt())
                .updatedAt(clothes.getUpdatedAt())
                .detailImg(clothes.getImg())
                .likeCount(clothes.getLikeCount())
                .clothesName(clothes.getName())
                .preview(clothes.getPreview())
                .designerId(designer.getId())
                .designerName(designer.getUserName())
                .score(clothes.getScore())
                .build();
    }

}
