package com.moola.fx.moneychanger.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.moola.fx.moneychanger.operations")
public class MoneyChangerApiApplication {

    // Set default JVM timezone to Singapore at app startup
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Singapore"));
        System.out.println(">>> TimeZone set to: " + TimeZone.getDefault());
    }

    public static void main(String[] args) {
        SpringApplication.run(MoneyChangerApiApplication.class, args);
    }
}