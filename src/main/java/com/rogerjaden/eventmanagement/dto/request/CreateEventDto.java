package com.rogerjaden.eventmanagement.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventDto {
    private String title;

    private Long categoryId;

    private LocalDateTime startTime;

    private Double price;

    private Integer remainingSeats;

    private String location;
}
