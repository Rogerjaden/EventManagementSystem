package com.rogerjaden.eventmanagement.service.interfaces;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.entity.Event;

import java.nio.file.AccessDeniedException;

public interface EventService {
    Event addEvents(CreateEventDto createEventDto) throws AccessDeniedException;
    Event getEvents();
    Event getEventById(long id);
    Event editEvent(long id, UpdateEventDto updateEventDto) throws AccessDeniedException;
    String deleteEvent(long id) throws AccessDeniedException;
}
