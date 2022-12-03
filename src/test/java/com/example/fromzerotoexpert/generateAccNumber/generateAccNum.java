package com.example.fromzerotoexpert.generateAccNumber;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author RabbitFaFa
 * @date 2022/12/3
 */
public class generateAccNum {
    @Test
    public void test() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("d" + calendar.get(Calendar.DAY_OF_YEAR) + "_" + Long.toHexString(System.currentTimeMillis()));
    }
}
