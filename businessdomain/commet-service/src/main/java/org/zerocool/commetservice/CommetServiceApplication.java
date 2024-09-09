package org.zerocool.commetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.zerocool.commetservice", "org.zerocool.sharedlibrary"})
public class CommetServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommetServiceApplication.class, args);
    }
}
