package com.code.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// AFTER
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class StoreApplication {

//    @PostConstruct
//    public void init() {
//
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
//        System.out.println("Spring application running in Asia/Kolkata timezone");
//    }

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

}