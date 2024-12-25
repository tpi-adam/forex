package com.tpi.forexapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForexApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForexApiApplication.class, args);
    }

}
