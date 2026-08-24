package com.rogerjaden.eventmanagement.service;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.entity.Category;
import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.entity.User;
import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import com.rogerjaden.eventmanagement.entity.enums.Role;
import com.rogerjaden.eventmanagement.mapper.EventMapper;
import com.rogerjaden.eventmanagement.repository.CategoryRepository;
import com.rogerjaden.eventmanagement.repository.EventRepository;
import com.rogerjaden.eventmanagement.repository.UserRepository;
import com.rogerjaden.eventmanagement.service.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    CategoryRepository categoryRepository;
    EventMapper eventMapper;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    public Event addEvents(CreateEventDto createEventDto) throws AccessDeniedException {
        Category category = categoryRepository.findById(createEventDto.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        User user = getCurrentUser();
        if(user.getRole() == Role.ORGANIZER){
            throw new AccessDeniedException("You are not an organizer");
        }
        Event event = eventMapper.toEntity(createEventDto, category);
        event.setOrganizer(user);
        return eventRepository.save(event);
    }

    @Override
    public Event getEvents() {
        return (Event) eventRepository.findAll();
    }

    @Override
    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Event editEvent(long id, UpdateEventDto updateEventDto) throws AccessDeniedException {
        User user = getCurrentUser();
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        validateOwnership(event, user);

        event.setTitle(updateEventDto.getTitle());
        event.setStartTime(updateEventDto.getStartTime());
        event.setPrice(updateEventDto.getPrice());
        event.setRemainingSeats(updateEventDto.getRemainingSeats());
        event.setLocation(updateEventDto.getLocation());
        return event;
    }

    @Override
    public String deleteEvent(long id) throws AccessDeniedException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User currentUser = getCurrentUser();

        validateOwnership(event, currentUser);

        event.setDeleted(true);
        event.setEventStatus(EventStatus.DEACTIVE);

        eventRepository.save(event);

        return "Event deleted successfully";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return user;
    }

    private void validateOwnership(Event event, User currentUser) throws AccessDeniedException {

        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(
                    "You are not the owner of this event"
            );
        }
    }
}
