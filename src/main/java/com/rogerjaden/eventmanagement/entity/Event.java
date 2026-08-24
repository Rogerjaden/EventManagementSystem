package com.rogerjaden.eventmanagement.entity;


import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime startTime;
    private double price;
    private int remainingSeats;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    private LocalDateTime createdAt;
    private String location;

}
