package pl.test.events.repositories;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.test.events.models.Event;
import pl.test.events.models.dto.EmailNotification;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUsersEmail(String email);

    @Query("SELECT e FROM Event e WHERE e.name = :name")
    Optional<Event> findByName(String name);

    Boolean existsEventByName(String name);

    void deleteEventByName(String name);

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

    @Modifying
    @Query(value = "UPDATE project.user_event SET is_sent = true WHERE user_email = ?1 AND event_id = ?2", nativeQuery = true)
    void updateSent(String email, Integer id);
}
