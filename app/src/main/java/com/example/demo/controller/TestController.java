package com.example.demo.controller;

import com.example.demo.service.BookService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<byte[]> memoryHog = new ArrayList<>();

    private final BookService bookService;

    public TestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/test-sleep")
    String testSleepWithoutTransactional(@RequestParam(defaultValue = "1") int seconds)
            throws InterruptedException {
        accessBooks(seconds);
        return "Done";
    }

    @Transactional
    @GetMapping("/test-sleep-1")
    String testSleepWithTransaction(@RequestParam(defaultValue = "1") int seconds)
            throws InterruptedException {
        accessBooks(seconds);
        return "Done";
    }

    private void accessBooks(int seconds) throws InterruptedException {
        try {
            Thread.sleep(seconds * 1000L);
            bookService.findAllBooksWithReviews(0, 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw e;
        }
    }

    @GetMapping("/trigger-oom")
    String outOfMemoryTest() {
        try {
            do {
                memoryHog.add(new byte[1024 * 1024]); // 1MB per object
                logger.info("Allocated {} MB", memoryHog.size());
            } while (memoryHog.size() != 1000);
        } catch (OutOfMemoryError e) {
            logger.error(
                    "Caught OutOfMemoryError: {}, List size: {}", e.getMessage(), memoryHog.size());
        }
        return "Done";
    }
}
