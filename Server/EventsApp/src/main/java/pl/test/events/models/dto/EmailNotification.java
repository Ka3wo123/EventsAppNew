package pl.test.events.models.dto;

import java.sql.Date;
import java.sql.Time;

public record EmailNotification(Integer id, String name, Time timeOfEvent, Date dateOfEvent, String placeOfEvent, String email, Boolean isSent) {
}
