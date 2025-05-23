package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
public class MessageContext {
    private String id;
    private String name;

    @Override
    public String toString() {
        return "MyData{id='" + id + "', name='" + name + "'}";
    }
}
