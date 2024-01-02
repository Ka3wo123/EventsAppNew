package pl.test.events.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.test.events.models.Event;
import pl.test.events.models.dto.EmailNotification;
import pl.test.events.models.dto.EventDto;
import pl.test.events.models.dto.EventDtoMapper;
import pl.test.events.repositories.EventRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventDtoMapper eventDtoMapper;
    private final JavaMailSenderImpl emailSender;

    @Autowired
    public EventService(EventRepository eventRepository, EventDtoMapper eventDtoMapper, JavaMailSenderImpl emailSender) {
        this.eventRepository = eventRepository;
        this.eventDtoMapper = eventDtoMapper;
        this.emailSender = emailSender;
    }

    /**
     *
     * @param email User's email.
     * @return All events or events for user's email or event location.
     */
    public List<EventDto> getAllEvents(String email, String location) {
        if (email != null && location == null) {
            return eventRepository.findByUsersEmail(email).stream().map(eventDtoMapper).toList();
        } else if(email == null && location != null) {
            return eventRepository.findByPlace(location).stream().map(eventDtoMapper).toList();
        }
        return eventRepository.findAll().stream().map(eventDtoMapper).toList();
    }


    /**
     * User can add new event.
     * @param eventDto Event entity as DTO.
     * @return Information regard to creation of event.
     */
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

    /**
     *
     * @param name Event name which will be deleted from set of events.
     * @return Information regard to deletion of event.
     */
    @Transactional
    public ResponseEntity<String> deleteEvent(String name) {
        if (eventRepository.existsEventByName(name)) {
            eventRepository.deleteEventByName(name);
            return new ResponseEntity<>("Event deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("No such event", HttpStatus.CONFLICT);
    }

    /**
     * Method that is invoked in specific time when server is running.
     * It sends reminder notifications on users' emails who have been assigned to
     * particular events. Reminders are sent one week before event's date.
     */
    @Transactional
    @Scheduled(fixedRate = 5000)
    public void sendEmails() {
        System.out.println("=======: Sending notifications :========");
        List<EmailNotification> emailsNotification = getEmailsNotification();

        LocalDate currentDate = LocalDate.now();

        for (EmailNotification data : emailsNotification) {

            if (data.isSent()) {
                continue;
            }

            LocalDate eventDate = LocalDate.parse(data.dateOfEvent().toString());

            if (currentDate.plusDays(7).isAfter(eventDate) || currentDate.plusDays(7).isEqual(eventDate)) {
                String subject = "Nadchodzące wydarzenie: " + data.name();
                String message = "Już niedługo odbędzie się wydarzenie " + data.name() + ", na które się zapisałeś.\n\n" +
                        "Termin: " + data.dateOfEvent() + "\n" +
                        "Godzina: " + data.timeOfEvent() + "\n" +
                        "Miejsce: " + data.placeOfEvent();
                sendEmail(data.email(), subject, message);
                eventRepository.updateSent(data.email(), data.id());
            }
        }
        System.out.println("=======: Finished :========");
    }

    private List<EmailNotification> getEmailsNotification() {
        List<Object[]> result = eventRepository.getEmailsNotification();

        return result.stream()
                .map(this::mapToEmailNotification)
                .collect(Collectors.toList());
    }

    private EmailNotification mapToEmailNotification(Object[] result) {
        Integer id = (Integer) result[0];
        String eventName = (String) result[1];
        Time timeOfEvent = (Time) result[2];
        Date dateOfEvent = (Date) result[3];
        String placeOfEvent = (String) result[4];
        String userEmail = (String) result[5];
        Boolean isSent = (Boolean) result[6];

        return new EmailNotification(id, eventName, timeOfEvent, dateOfEvent, placeOfEvent, userEmail, isSent);
    }

    private void sendEmail(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
