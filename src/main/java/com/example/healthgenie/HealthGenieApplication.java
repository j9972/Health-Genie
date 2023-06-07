package com.example.healthgenie;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.SpringSecurityCoreVersion;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableEncryptableProperties // jasypt 를 위함
@EnableJpaAuditing
public class HealthGenieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthGenieApplication.class, args);
    }

}
