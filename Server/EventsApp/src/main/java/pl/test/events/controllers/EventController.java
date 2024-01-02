package pl.test.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.test.events.models.Event;
import pl.test.events.models.dto.EventDto;
import pl.test.events.models.dto.UserDto;
import pl.test.events.services.EventService;

import java.util.List;

@RestController
@RequestMapping("/event-app")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(value = "user-email", required = false) String email,
                                    @RequestParam(value = "event-location", required = false) String location) {
        return eventService.getAllEvents(email, location);
    }

    @PostMapping("/event/add")
    public ResponseEntity<String> createEvent(@RequestBody EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @DeleteMapping("/event/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam String name) {
        return eventService.deleteEvent(name);
    }


}
