package com.rogerjaden.eventmanagement.entity;

import com.rogerjaden.eventmanagement.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private BigDecimal price;
    private int seatCount;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
}
