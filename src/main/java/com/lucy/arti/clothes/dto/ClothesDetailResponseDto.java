package com.lucy.arti.clothes.dto;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.designer.domain.Designer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClothesDetailResponseDto {
    private Long clothesId;
    private Long likeCount;
    private String clothesName;
    private String preview;
    private String detailImg;
    private Long designerId;
    private String designerName;
    private long score;
    private Type type;
    private String purchaseLink;
    private int commentCount;

    public static ClothesDetailResponseDto of(Clothes clothes, Designer designer) {
        return ClothesDetailResponseDto.builder()
            .type(clothes.getType())
            .clothesId(clothes.getId())
            .clothesName(clothes.getName())
            .detailImg(clothes.getImg())
            .preview(clothes.getPreview())
            .designerId(designer.getId())
            .designerName(designer.getUserName())
            .likeCount(clothes.getLikeCount())
            .score(clothes.getScore())
            .purchaseLink(clothes.getLink())
            .commentCount(clothes.getCommentCountByClothes(clothes.getId()))
            .build();

    }
}
