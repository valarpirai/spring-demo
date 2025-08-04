package com.example.demo.service;

import com.example.demo.dto.MessageContext;
import com.example.demo.proto.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, MessageContext> kafkaTemplate;
    private final KafkaTemplate<String, byte[]> protobufKafkaTemplate;

    @Value("${spring.kafka.consumer.topic}")
    private String kafkaTopic;

    @Value("${spring.kafka.consumer.protobuf-topic:user-protobuf-topic}")
    private String protobufTopic;

    public KafkaProducerService(KafkaTemplate<String, MessageContext> kafkaTemplate, 
                               KafkaTemplate<String, byte[]> protobufKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.protobufKafkaTemplate = protobufKafkaTemplate;
    }

    public void sendMessage(MessageContext message) {
        kafkaTemplate.send(kafkaTopic, message.getId(), message);
        logger.info("Message sent on Topic {} : {}", kafkaTopic, message);
    }

    public void sendProtobufMessage(UserMessage userMessage) {
        try {
            byte[] messageBytes = userMessage.toByteArray();
            protobufKafkaTemplate.send(protobufTopic, userMessage.getId(), messageBytes);
            logger.info("Protobuf message sent on Topic {} : {}", protobufTopic, userMessage);
        } catch (Exception e) {
            logger.error("Error sending protobuf message: ", e);
            throw new RuntimeException("Failed to send protobuf message", e);
        }
    }
}
