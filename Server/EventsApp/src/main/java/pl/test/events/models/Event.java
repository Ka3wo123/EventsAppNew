package pl.test.events.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "event", schema = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            nullable = false)
    private Long id;
    @Column(name = "name",
            nullable = false,
            columnDefinition = "VARCHAR(200) NOT NULL")
    private String name;
    @Column(name = "place_of_event",
            nullable = false,
            columnDefinition = "VARCHAR(100) NOT NULL")
    private String place;
    @Column(name = "date_of_event",
            nullable = false,
            columnDefinition = "DATE NOT NULL")
    private LocalDate date;
    @Column(name = "time_of_event",
            nullable = false,
            columnDefinition = "TIME NOT NULL")
    private LocalTime time;
    @Column(name = "max_sites")
    private Integer maxSites;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_event", schema = "project",
            joinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_email", referencedColumnName = "email")
            })
    private List<User> users;
}