package com.rogerjaden.eventmanagement.service;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.EventSearchRequest;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.dto.response.EventResponse;
import com.rogerjaden.eventmanagement.entity.Category;
import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.entity.User;
import com.rogerjaden.eventmanagement.entity.enums.EventStatus;
import com.rogerjaden.eventmanagement.entity.enums.Role;
import com.rogerjaden.eventmanagement.mapper.EventMapper;
import com.rogerjaden.eventmanagement.mapper.EventSpecification;
import com.rogerjaden.eventmanagement.repository.CategoryRepository;
import com.rogerjaden.eventmanagement.repository.EventRepository;
import com.rogerjaden.eventmanagement.repository.UserRepository;
import com.rogerjaden.eventmanagement.service.interfaces.EventService;
import com.rogerjaden.eventmanagement.service.interfaces.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageService imageService;

    @Override
    public EventResponse addEvents (CreateEventDto createEventDto, MultipartFile banner) {
        Category category = categoryRepository.findById(createEventDto.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        User user = getCurrentUser();

        Event event = eventMapper.toEntity(createEventDto, category);
        event.setOrganizer(user);
        String bannerUrl =
                imageService.store(banner);

        event.setBannerUrl(bannerUrl);
        eventRepository.save(event);
        return new EventResponse(event.getId(),
                                event.getTitle(),
                                event.getCategory().getCategoryName(),
                                event.getStartTime(),
                                event.getPrice(),
                                event.getRemainingSeats(),
                                event.getBannerUrl()
        );
    }

    @Override
    public Page<EventResponse> getEvents(Pageable pageable) {

        Page<Event> events =
                eventRepository.findByIsDeletedFalseAndEventStatus(
                        EventStatus.ACTIVE,
                        pageable
                );

        return events.map(event -> new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getCategory().getCategoryName(),
                event.getStartTime(),
                event.getPrice(),
                event.getRemainingSeats(),
                event.getBannerUrl()
        ));
    }

    @Override
    public EventResponse getEventById (long id) {
        Event event = eventRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return new EventResponse(event.getId(),
                                event.getTitle(),
                                event.getCategory().getCategoryName(),
                                event.getStartTime(),
                                event.getPrice(),
                                event.getRemainingSeats(),
                                event.getBannerUrl()
        );
    }

    @Override
    public EventResponse editEvent (long id, UpdateEventDto updateEventDto) throws AccessDeniedException {
        User user = getCurrentUser();
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        validateOwnership(event, user);

        if(!Objects.equals(updateEventDto.getTitle(), event.getTitle()) &&
                !Objects.equals(updateEventDto.getTitle(), "string")){
            event.setTitle(updateEventDto.getTitle());
        }
        if(!Objects.equals(updateEventDto.getStartTime(), event.getStartTime()) &&
                !Objects.equals(updateEventDto.getStartTime(), LocalDateTime.now())){
            event.setStartTime(updateEventDto.getStartTime());
        }
        if(updateEventDto.getPrice() != event.getPrice() &&
                updateEventDto.getPrice() != null){
            event.setPrice(updateEventDto.getPrice());
        }
        if(updateEventDto.getRemainingSeats() != event.getRemainingSeats() &&
                updateEventDto.getRemainingSeats() > 0){
            event.setRemainingSeats(updateEventDto.getRemainingSeats());
        }
        if(!Objects.equals(updateEventDto.getLocation(), event.getLocation()) &&
                !Objects.equals(updateEventDto.getLocation(),"string")){
            event.setLocation(updateEventDto.getLocation());
        }
        eventRepository.save(event);
        return new EventResponse(event.getId(),
                event.getTitle(),
                event.getCategory().getCategoryName(),
                event.getStartTime(),
                event.getPrice(),
                event.getRemainingSeats(),
                event.getBannerUrl()
        );
    }

    @Override
    public String deleteEvent (long id) throws AccessDeniedException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User currentUser = getCurrentUser();

        validateOwnership(event, currentUser);

        event.setDeleted(true);
        event.setEventStatus(EventStatus.DEACTIVE);

        eventRepository.save(event);

        return "Event deleted successfully";
    }

    private User getCurrentUser () {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    private void validateOwnership(Event event, User currentUser) throws AccessDeniedException {

        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        if (currentUser.getRole() == Role.ORGANIZER &&
                event.getOrganizer().getId().equals(currentUser.getId())) {
            return;
        }

        throw new AccessDeniedException(
                "You are not allowed to modify this event"
        );
    }

    public Page<EventResponse> search(EventSearchRequest request, Pageable pageable) {
        Page<Event> events = eventRepository.findAll(
                EventSpecification.withFilters(request),pageable
        );
        return events.map(event -> new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getCategory().getCategoryName(),
                event.getStartTime(),
                event.getPrice(),
                event.getRemainingSeats(),
                event.getBannerUrl()
        ));
    }
}
