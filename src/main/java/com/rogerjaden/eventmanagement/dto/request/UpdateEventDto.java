package com.rogerjaden.eventmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateEventDto {

    private String title;

    private LocalDateTime startTime;

    private BigDecimal price;

    private int remainingSeats;

    private String location;
}