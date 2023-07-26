package com.ausn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling //定时将redis中的数据同步到mysql
@EnableTransactionManagement
public class PiliPiliApplication
{
    public static void main(String[] args) {
        SpringApplication.run(PiliPiliApplication.class, args);
    }
}
