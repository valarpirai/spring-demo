package com.example.demo.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/")
    Map<String, String> index() {
        logger.trace("This is a trace message");
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");

        return Map.of("message", "Hello, World!");
    }

    @GetMapping("/health")
    String health() {
        logger.info("Health path");
        return "Ok";
    }
}
