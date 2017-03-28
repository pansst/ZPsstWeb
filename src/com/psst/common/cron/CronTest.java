package com.psst.common.cron;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

public class CronTest {
    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String expression = "0/10 * * * * *";
        CronTrigger trigger = new CronTrigger(expression);
        CronSequenceGenerator generator = new CronSequenceGenerator(expression);
        int i = 0;
        while( i ++ < 10) {
            date = generator.next(date);
            System.out.println(sdf.format(date));
        }
    }
}
