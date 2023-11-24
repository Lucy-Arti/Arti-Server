package com.lucy.arti.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private PointService pointService;

    //    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 0 3 * * ?")
    public void resetDailyFlags() {
        pointService.updateDailyToTrue();
    }

    // 이 메서드는 매주 화요일 00:00:00에 호출됩니다.
    @Scheduled(cron = "0 0 0 * * TUE")
    public void tueDailyFlags() {
        pointService.updateTueToTrue();
    }

    @Scheduled(cron = "0 0 0 L * ?")
    public void resetMonthlyTotal() {
        pointService.updateMonthToTrue();
    }


}
