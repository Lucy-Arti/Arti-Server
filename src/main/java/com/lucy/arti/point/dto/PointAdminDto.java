package com.lucy.arti.point.dto;

import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerDetailResponseDto;
import com.lucy.arti.point.domain.Point;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointAdminDto {

    private Long memberId;
    private Long currentPoint;
    private Long accumulatedGivenPoint;
    private Long accumulatedUsedPoint;

    public static PointAdminDto of(Long memberId, Long currentPoint
        , Long givenPoint, Long usedPoint) {
        return PointAdminDto.builder()
            .memberId(memberId)
            .currentPoint(currentPoint)
            .accumulatedGivenPoint(givenPoint)
            .accumulatedUsedPoint(usedPoint)
            .build();
    }

}
