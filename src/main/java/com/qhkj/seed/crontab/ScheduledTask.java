package com.qhkj.seed.crontab;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    @Scheduled(cron = "0 0 3 * * ?")
    public void reportCurrentTimeCron() throws InterruptedException {
        System.out.println("3点定时任务");
    }
}
