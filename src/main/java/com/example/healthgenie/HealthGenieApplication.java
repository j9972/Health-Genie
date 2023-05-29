package com.example.healthgenie;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties // jasypt 를 위함
public class HealthGenieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthGenieApplication.class, args);
    }

}
