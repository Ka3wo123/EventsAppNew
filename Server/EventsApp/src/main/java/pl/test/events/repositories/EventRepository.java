package pl.test.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.test.events.models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUsersEmail(String email);
    @Query("SELECT e FROM Event e WHERE e.name = :name")
    Optional<Event> findByName(String name);

    Boolean existsEventByName(String name);

    void deleteEventByName(String name);
}
