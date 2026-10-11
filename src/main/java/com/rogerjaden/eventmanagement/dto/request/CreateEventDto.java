package com.rogerjaden.eventmanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateEventDto {
    @NotBlank
    private String title;

    @NotNull
    private Long categoryId;

    @NotNull
    @Future
    private LocalDateTime startTime;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer remainingSeats;

    @NotBlank
    private String location;
}
