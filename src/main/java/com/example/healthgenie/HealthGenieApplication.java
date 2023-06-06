package com.example.healthgenie;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableEncryptableProperties // jasypt 를 위함
@EnableJpaAuditing
public class HealthGenieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthGenieApplication.class, args);
    }

}
