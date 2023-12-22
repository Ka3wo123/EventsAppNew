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

    @Modifying
    @Query(value = "INSERT INTO project.user_event (user_email, event_id) VALUES (?1, ?2)", nativeQuery = true)
    void addUserToEvent(String userEmail, Long eventId);

    @Modifying
    @Query(value = "DELETE FROM project.user_event WHERE user_email = ?1 AND event_id = ?2", nativeQuery = true)
    void deleteEventForUser(String email, Long id);
}
