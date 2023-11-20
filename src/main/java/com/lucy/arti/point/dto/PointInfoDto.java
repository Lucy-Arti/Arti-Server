package com.lucy.arti.point.dto;

import com.lucy.arti.point.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointInfoDto {

    private Long point;
    private MissionDto mission;

    public PointInfoDto(Point point) {
        this.point = point.getPoint();
        this.mission = new MissionDto(point);
    }

    @Getter
    @NoArgsConstructor
    public static class MissionDto {
        private boolean comment;
        private boolean vote;
        private boolean visit;
        private boolean follow;
        private boolean story;
        private boolean friend;

        public MissionDto(Point point) {
            this.comment = point.isComment();
            this.vote = point.isVote();
            this.visit = point.isVisit();
            this.follow = point.isFollow();
            this.story = point.isStory();
            this.friend = point.isFriend();
        }
    }
}