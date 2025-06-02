package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<byte[]> memoryHog = new ArrayList<>();

    @Autowired
    private BookService bookService;

    @GetMapping("/test-sleep")
    String testSleep(@RequestParam(defaultValue = "1") int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            bookService.findAllBooksWithReviews(0, 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Done";
    }

    @GetMapping("/trigger-oom")
    String outOfMemoryTest() {
        try {
            while (true) {
                memoryHog.add(new byte[1024 * 1024]); // 1MB per object
                logger.info("Allocated {} MB", memoryHog.size());
            }
        } catch (OutOfMemoryError e) {
            logger.error("Caught OutOfMemoryError: {}, List size: {}", e.getMessage(), memoryHog.size());
        }
        return "Done";
    }

    @Transactional
    @GetMapping("/test-sleep-1")
    String testSleepWithTransaction(@RequestParam(defaultValue = "1") int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            bookService.findAllBooksWithReviews(0, 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Done";
    }
}
