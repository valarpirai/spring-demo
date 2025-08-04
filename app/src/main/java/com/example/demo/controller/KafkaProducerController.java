package com.example.demo.controller;

import com.example.demo.dto.MessageContext;
import com.example.demo.proto.MessageType;
import com.example.demo.proto.UserMessage;
import com.example.demo.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaProducerService producerService;

    public KafkaProducerController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public String produceKafkaMessage(@RequestBody MessageContext message) {
        logger.info("Sending message: {}", message);
        producerService.sendMessage(message);
        return "Message sent: " + message;
    }

    @PostMapping("/send-protobuf")
    public String produceProtobufMessage(@RequestBody ProtobufMessageRequest request) {
        logger.info("Sending protobuf message: {}", request);
        
        UserMessage userMessage = UserMessage.newBuilder()
                .setId(request.getId())
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setMessage(request.getMessage())
                .setTimestamp(System.currentTimeMillis())
                .setType(MessageType.valueOf(request.getType().toUpperCase()))
                .build();
        
        producerService.sendProtobufMessage(userMessage);
        return "Protobuf message sent: " + request;
    }

    public static class ProtobufMessageRequest {
        private String id;
        private String username;
        private String email;
        private String message;
        private String type;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        @Override
        public String toString() {
            return "ProtobufMessageRequest{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", message='" + message + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
