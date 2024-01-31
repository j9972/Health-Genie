package com.example.healthgenie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HealthGenieApplication {

    public static void main(String[] args) {

        SpringApplication.run(HealthGenieApplication.class, args);
    }
}
