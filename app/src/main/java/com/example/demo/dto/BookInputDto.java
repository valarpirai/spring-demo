package com.example.demo.dto;

import java.time.LocalDate;

public record BookInputDto(
        String title,
        String author,
        LocalDate publicationDate
) {
}
