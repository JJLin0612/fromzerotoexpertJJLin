package com.example.fromzerotoexpert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FromZeroToExpertApplication {

    public static void main(String[] args) {
        SpringApplication.run(FromZeroToExpertApplication.class, args);
    }

}
