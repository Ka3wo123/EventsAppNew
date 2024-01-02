package pl.test.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.test.events.models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUsersEmail(String email);

    /**
     *
     * @return Event details.
     */
    @Query("SELECT e FROM Event e WHERE e.name = :name")
    Optional<Event> findByName(String name);

    Boolean existsEventByName(String name);

    void deleteEventByName(String name);

    List<Event> findByPlace(String location);

    /**
     * Custom query which is necessary for notification sending. Operates on association table.
     * @return Event's and assigned user's data.
     */
    @Query(value = "SELECT e.id AS id, " +
            "e.name AS eventName," +
            " e.time_of_event AS timeOfEvent," +
            " e.date_of_event AS dateOfEvent," +
            " e.place_of_event AS placeOfEvent," +
            " ue.user_email AS userEmail," +
            " ue.is_sent AS isSent " +
            "FROM project.user_event AS ue " +
            "INNER JOIN project.event AS e ON e.id = ue.event_id", nativeQuery = true)
    List<Object[]> getEmailsNotification();

    /**
     * Custom query which provides that notification is sent to user. Operates on association table.
     * @param email User's email who is assigned to event.
     * @param id Event identifier.
     */
    @Modifying
    @Query(value = "UPDATE project.user_event SET is_sent = true WHERE user_email = ?1 AND event_id = ?2", nativeQuery = true)
    void updateSent(String email, Integer id);
}
