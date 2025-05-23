package com.example.demo.service;

import com.example.demo.dto.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String, MessageContext> kafkaTemplate;

    @Value("${spring.kafka.consumer.topic}")
    private String kafkaTopic;

    public void sendMessage(MessageContext message) {
        kafkaTemplate.send(kafkaTopic, message.getId(), message);
        logger.info("Message sent on Topic {} : {}", kafkaTopic, message);
    }
}
