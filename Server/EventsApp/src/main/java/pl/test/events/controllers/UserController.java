package pl.test.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.test.events.models.dto.UserDto;
import pl.test.events.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/event-app")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public ResponseEntity<String> createAccount(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PostMapping("/user/add-event")
    public ResponseEntity<String> addEventForUser(@RequestParam String email, @RequestParam String name) {
        return userService.addUserToEvent(email, name);
    }


    @GetMapping("/user/validate")
    public Boolean validateCredentials(@RequestParam(name = "email") String email,
                                       @RequestParam(name = "password") char[] password) {
        return userService.validateCredentials(email, password);
    }

    @DeleteMapping("/user/delete-event")
    public ResponseEntity<String> deleteEventForUser(@RequestParam(name = "email") String email,
                                                     @RequestParam(name = "name") String name) {
        return userService.deleteEventForUser(email, name);
    }

}
