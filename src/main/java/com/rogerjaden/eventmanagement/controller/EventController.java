package com.rogerjaden.eventmanagement.controller;

import com.rogerjaden.eventmanagement.dto.request.CreateEventDto;
import com.rogerjaden.eventmanagement.dto.request.EventSearchRequest;
import com.rogerjaden.eventmanagement.dto.request.UpdateEventDto;
import com.rogerjaden.eventmanagement.dto.response.EventResponse;
import com.rogerjaden.eventmanagement.service.interfaces.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Operation(
            summary = "Create an event",
            description = "Creates an even for the authenticated organizer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid event details"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @PostMapping(
            value = "/addEvents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @RequestBody(
            content = @Content(
                    encoding = @Encoding(
                            name = "event",
                            contentType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    )
    public EventResponse addEvents(
            @RequestPart("event")
            @Valid
            CreateEventDto createEventDto,

            @RequestPart("banner")
            MultipartFile banner
    ) {
        return eventService.addEvents(createEventDto, banner);
    }

    @GetMapping("/getEvents")
    public ResponseEntity<?> getEvents(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                       @RequestParam(defaultValue = "true") boolean ascending){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return ResponseEntity.ok(eventService.getEvents(pageable));
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

    @GetMapping("/search")
    public ResponseEntity<Page<EventResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100"
            );
        }

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        EventSearchRequest request =
                new EventSearchRequest(
                        title,
                        categoryId,
                        date,
                        location,
                        minPrice,
                        maxPrice
                );

        return ResponseEntity.ok(
                eventService.search(request, pageable)
        );
    }
}
