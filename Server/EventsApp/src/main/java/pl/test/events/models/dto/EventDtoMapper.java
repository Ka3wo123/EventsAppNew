package pl.test.events.models.dto;

import org.springframework.stereotype.Service;
import pl.test.events.models.Event;

import java.util.function.Function;

@Service
public class EventDtoMapper implements Function<Event, EventDto> {
    @Override
    public EventDto apply(Event event) {
        return new EventDto(event.getName(),
                event.getPlace(),
                event.getDate(),
                event.getMaxSites(),
                event.getTime());

    }


}
