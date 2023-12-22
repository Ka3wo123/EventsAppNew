package pl.test.events.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.test.events.models.Event;
import pl.test.events.models.dto.EventDto;
import pl.test.events.models.dto.EventDtoMapper;
import pl.test.events.repositories.EventRepository;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventDtoMapper eventDtoMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventDtoMapper eventDtoMapper) {
        this.eventRepository = eventRepository;
        this.eventDtoMapper = eventDtoMapper;
    }

    public List<EventDto> getAllEvents(String email) {
        if (email != null) {
            return eventRepository.findByUsersEmail(email).stream().map(eventDtoMapper).toList();
        }
        return eventRepository.findAll().stream().map(eventDtoMapper).toList();
    }

    public ResponseEntity<String> createEvent(EventDto eventDto) {
        if (eventRepository.existsEventByName(eventDto.name())) {
            return new ResponseEntity<>("Event " + eventDto.name() + " already exists", HttpStatus.CONFLICT);
        }

        Event event = new Event();
        event.setName(eventDto.name());
        event.setPlace(eventDto.placeOfEvent());
        event.setDate(eventDto.dateOfEvent());
        event.setMaxSites(eventDto.maxSites());
        event.setTime(eventDto.timeOfEvent());

        eventRepository.save(event);

        return new ResponseEntity<>("Event " + eventDto.name() + " created!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> deleteEvent(String name) {
        if(eventRepository.existsEventByName(name)) {
            eventRepository.deleteEventByName(name);
            return new ResponseEntity<>("Event deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("No such event", HttpStatus.CONFLICT);
    }

}
