package com.sahithya.fooddeliverybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FoodDeliveryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryBackendApplication.class, args);
    }

}
