package com.example.demo.consumer;

import com.example.demo.dto.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(MessageContext message) {
        if (message != null) {
            logger.info("Message received: {}", message);
        } else {
            logger.warn("Received null message due to deserialization failure");
        }
    }
}
