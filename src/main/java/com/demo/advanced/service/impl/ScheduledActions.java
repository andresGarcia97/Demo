package com.demo.advanced.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class ScheduledActions {

    @Scheduled(cron = "${application.scheduled.clean}")
    public void cronScheduled(){
        log.info("Scheduled at {}", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Scheduled(fixedDelayString = "${application.scheduled.interval}")
    public void intervalScheduled(){
        log.info("Interval at {}", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

}
