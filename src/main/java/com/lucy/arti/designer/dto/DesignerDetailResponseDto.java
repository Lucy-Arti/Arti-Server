package com.lucy.arti.designer.dto;

import com.lucy.arti.designer.domain.Designer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DesignerDetailResponseDto {
   private String userName;
   private String introduce;
   private String instagram;
   private String designerProfile;

    public static DesignerDetailResponseDto of(Designer designer) {
        return DesignerDetailResponseDto.builder()
            .userName(designer.getUserName())
            .introduce(designer.getIntroduce())
            .instagram(designer.getInstagram())
            .designerProfile(designer.getDesignerProfile())
            .build();
    }

}
