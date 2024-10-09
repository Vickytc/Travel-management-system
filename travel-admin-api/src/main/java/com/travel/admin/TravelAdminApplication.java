package com.travel.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Startup Class
 */
@SpringBootApplication
@MapperScan("com.travel.admin.mapper")
public class TravelAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelAdminApplication.class, args);
    }

}
