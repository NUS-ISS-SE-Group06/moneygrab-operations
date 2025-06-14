package com.moola.fx.moneychanger.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.moola.fx.moneychanger.operations")
public class MoneyChangerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyChangerApiApplication.class, args);
    }

}