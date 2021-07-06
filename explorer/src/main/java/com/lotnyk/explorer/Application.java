package com.lotnyk.explorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.lotnyk.explorer"})
@PropertySource("classpath:file.properties")
public class Application {

    public static void main(String[]args) {
        SpringApplication.run(Application.class, args);
    }
}
