package com.rogerjaden.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String message;
    private boolean isRead;
}
