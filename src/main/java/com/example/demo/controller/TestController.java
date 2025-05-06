package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

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
