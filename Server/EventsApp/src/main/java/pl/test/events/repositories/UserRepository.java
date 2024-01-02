package pl.test.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.test.events.models.Event;
import pl.test.events.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<Event> findEventByEmail(String email);
    Optional<User> findByEmail(String email);
    Boolean existsUserByEmail(String email);

    /**
     * Custom query which assigns user to event. Operates on association table.
     * @param userEmail User's email who will be assigned to event.
     * @param eventId Event identifier.
     */
    @Modifying
    @Query(value = "INSERT INTO project.user_event (user_email, event_id, is_sent) VALUES (?1, ?2, false)", nativeQuery = true)
    void addUserToEvent(String userEmail, Long eventId);

    /**
     * Custom query which removes user from specific event. Operates on association table.
     * @param email User's email who will be removed from event.
     * @param id Event identifier.
     */
    @Modifying
    @Query(value = "DELETE FROM project.user_event WHERE user_email = ?1 AND event_id = ?2", nativeQuery = true)
    void deleteEventForUser(String email, Long id);
}
