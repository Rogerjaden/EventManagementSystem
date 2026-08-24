package com.rogerjaden.eventmanagement.mapper;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.entity.Category;
import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    public Event toEntity(CreateEventDto createEventDto, Category category){
        Event event = new Event();
        event.setTitle(createEventDto.getTitle());
        event.setCategory(category);
        event.setStartTime(createEventDto.getStartTime());
        event.setPrice(createEventDto.getPrice());
        event.setRemainingSeats(createEventDto.getRemainingSeats());
        event.setLocation(createEventDto.getLocation());
        event.setDeleted(false);
        event.setEventStatus(EventStatus.ACTIVE);
        event.setCreatedAt(LocalDateTime.now());
        return event;
    }
}
