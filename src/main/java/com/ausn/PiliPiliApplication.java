package com.ausn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //定时将redis中的数据同步到mysql
public class PiliPiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiliPiliApplication.class, args);
    }

}
