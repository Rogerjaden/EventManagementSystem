package com.rogerjaden.eventmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventResponse {
    private long id;
    private String title;
    private String categoryName;
    private LocalDateTime startTime;
    private BigDecimal price;
    private int remainingSeats;
    private String bannerUrl;

    public EventResponse(long id, String title, String categoryName, LocalDateTime startTime, BigDecimal price, int remainingSeats, String bannerUrl) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.startTime = startTime;
        this.price = price;
        this.remainingSeats = remainingSeats;
        this.bannerUrl = bannerUrl;
    }
}
