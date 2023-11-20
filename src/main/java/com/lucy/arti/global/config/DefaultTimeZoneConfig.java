package com.lucy.arti.global.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class DefaultTimeZoneConfig {
    private static final String KST = "Asia/Seoul";

    @PostConstruct
    public void start() {
        TimeZone.setDefault(TimeZone.getTimeZone(KST));
    }
}
