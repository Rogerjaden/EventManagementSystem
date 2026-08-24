package com.rogerjaden.eventmanagement.controller;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.entity.Event;
import com.rogerjaden.eventmanagement.service.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping(value = "events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvents")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public Event addEvents(@RequestBody CreateEventDto createEventDto) throws AccessDeniedException {
        return eventService.addEvents(createEventDto);
    }

    @GetMapping("/getEvents")
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok(eventService.getEvents());
    }

    @GetMapping("/getEvents/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<?> getEventById(@PathVariable long id){
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/editEvents/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<?> editEvent( @PathVariable long id, @RequestBody UpdateEventDto updateEventDto) throws AccessDeniedException {
        return ResponseEntity.ok(eventService.editEvent(id,updateEventDto));
    }

    @DeleteMapping("/deleteEvents/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable long id) throws AccessDeniedException {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }
}
