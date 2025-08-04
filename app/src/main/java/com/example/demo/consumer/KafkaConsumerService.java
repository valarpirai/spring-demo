package com.example.demo.consumer;

import com.example.demo.dto.MessageContext;
import com.example.demo.proto.UserMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(
            topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(MessageContext message) {
        if (message != null) {
            logger.info("Message received: {}", message);
        } else {
            logger.warn("Received null message due to deserialization failure");
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.protobuf-topic:user-protobuf-topic}",
            groupId = "${spring.kafka.consumer.group-id}-protobuf")
    public void consumeProtobufMessage(byte[] messageBytes) {
        try {
            if (messageBytes != null) {
                UserMessage userMessage = UserMessage.parseFrom(messageBytes);
                logger.info("Protobuf message received: ID={}, Username={}, Email={}, Message={}, Timestamp={}, Type={}", 
                          userMessage.getId(),
                          userMessage.getUsername(),
                          userMessage.getEmail(),
                          userMessage.getMessage(),
                          userMessage.getTimestamp(),
                          userMessage.getType());
            } else {
                logger.warn("Received null protobuf message");
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error("Error parsing protobuf message: ", e);
        } catch (Exception e) {
            logger.error("Unexpected error processing protobuf message: ", e);
        }
    }
}
