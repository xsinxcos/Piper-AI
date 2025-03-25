package com.zhuo.piper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        log.info("Starting Piper-AI Worker...");
        SpringApplication.run(Main.class, args);
        log.info("Piper-AI Worker started successfully.");
    }
}