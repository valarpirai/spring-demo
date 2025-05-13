package com.example.demo.eventlisteners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MyCustomEvent extends ApplicationEvent {
    private final String message;

    public MyCustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
