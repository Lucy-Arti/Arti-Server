package com.lucy.arti.point.dto;

import lombok.Getter;

@Getter
// PointValuesResponse.java
public class PointValuesResponse {
    private Long invited;
    private Long accumulated;

    public PointValuesResponse(Long invited, Long invitedTimes1000) {
        this.invited = invited;
        this.accumulated = invitedTimes1000;
    }

}
