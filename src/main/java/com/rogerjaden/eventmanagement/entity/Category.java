package com.rogerjaden.eventmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<Event> events;
}
