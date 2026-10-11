package com.rogerjaden.eventmanagement.service.interfaces;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.EventSearchRequest;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.dto.response.EventResponse;
import com.rogerjaden.eventmanagement.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface EventService {
    EventResponse addEvents(CreateEventDto createEventDto, MultipartFile banner);
    Page<EventResponse> getEvents(Pageable pageable);
    EventResponse getEventById(long id);
    EventResponse editEvent(long id, UpdateEventDto updateEventDto) throws AccessDeniedException;
    String deleteEvent(long id) throws AccessDeniedException;
    Page<EventResponse> search(EventSearchRequest request, Pageable pageable);
}
