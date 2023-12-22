package pl.test.events.models.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDto(String name, String placeOfEvent, LocalDate dateOfEvent, Integer maxSites, LocalTime timeOfEvent) {
}
