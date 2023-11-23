package com.lucy.arti.designer.dto;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.designer.domain.Designer;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DesignerDetailResponseDto {
   private String userName;
   private String introduce;
   private String instagram;
   private String designerProfile;
   private Map<Long, String> productsIdAndNameByDesigner;
   private Map<Long, String> sketchesIdAndNameByDesigner;
    public static DesignerDetailResponseDto of(Designer designer) {
        return DesignerDetailResponseDto.builder()
            .userName(designer.getUserName())
            .introduce(designer.getIntroduce())
            .instagram(designer.getInstagram())
            .designerProfile(designer.getDesignerProfile())
            .productsIdAndNameByDesigner(designer.getProductIdAndNames())
            .sketchesIdAndNameByDesigner(designer.getSketchIdAndNames())
            .build();
    }

}
