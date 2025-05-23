package com.example.demo.controller;

import com.example.demo.dto.MessageContext;
import com.example.demo.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/send")
    public String produceKafkaMessage(@RequestBody MessageContext message) {
        producerService.sendMessage(message);
        return "Message sent: " + message;
    }
}
