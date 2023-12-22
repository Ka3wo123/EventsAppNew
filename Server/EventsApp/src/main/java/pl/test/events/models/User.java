package pl.test.events.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "user", schema = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "email",
            nullable = false,
            columnDefinition = "VARCHAR(50)",
            unique = true)
    private String email;
    @Column(name = "name",
            nullable = false,
            columnDefinition = "VARCHAR(50)")
    private String name;
    @Column(name = "surname",
            nullable = false,
            columnDefinition = "VARCHAR(50)")
    private String surname;
    @Column(name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(100)")
    private char[] password;

    @ManyToMany(mappedBy = "users")
    private List<Event> events;

}
