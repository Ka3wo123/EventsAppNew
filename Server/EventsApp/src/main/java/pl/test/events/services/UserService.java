package pl.test.events.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.test.events.models.Event;
import pl.test.events.models.User;
import pl.test.events.models.dto.EventDtoMapper;
import pl.test.events.models.dto.UserDto;
import pl.test.events.models.dto.UserDtoMapper;
import pl.test.events.repositories.EventRepository;
import pl.test.events.repositories.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final EventRepository eventRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserDtoMapper userDtoMapper,
                       EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userDtoMapper).toList();
    }

    public ResponseEntity<String> createUser(UserDto userDto) {
        if (userRepository.existsUserByEmail(userDto.email())) {
            return new ResponseEntity<>("Email already exists. New user not created", HttpStatus.CONFLICT);
        }

        User user = new User();

        user.setName(userDto.name());
        user.setSurname(userDto.surname());
        user.setEmail(userDto.email());
        user.setPassword(msgDigestSHA256(userDto.password()).toCharArray());

        userRepository.save(user);

        return new ResponseEntity<>("User " + userDto.email() + " created!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> addUserToEvent(String userEmail, String name) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        Optional<Event> optionalEvent = eventRepository.findByName(name);

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            if (!user.getEvents().contains(event)) {
                userRepository.addUserToEvent(userEmail, event.getId());
                return new ResponseEntity<>(userEmail + " assigned to " + name, HttpStatus.OK);
            }
            return new ResponseEntity<>("User " + userEmail+ " hasn't been assigned to event " + name + " because it is already done", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Event " + name + " not found", HttpStatus.CONFLICT);
    }

    @Transactional
    public ResponseEntity<String> deleteEventForUser(String email, String name) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<Event> optionalEvent = eventRepository.findByName(name);

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            userRepository.deleteEventForUser(email, optionalEvent.get().getId());
            return new ResponseEntity<>("User " + email + " deleted from event " + name, HttpStatus.OK);
        }
        return new ResponseEntity<>("User " + email + " is not assigned to event " + name, HttpStatus.CONFLICT);
    }


    private Optional<char[]> getPasswordByEmail(String email) {
        Optional<User> repositoryUser = userRepository.findByEmail(email);

        return repositoryUser.map(User::getPassword);
    }

    public Boolean validateCredentials(String email, char[] password) {
        Optional<char[]> optionalRepoPassword = getPasswordByEmail(email);

        return optionalRepoPassword.map(repoPassword ->
                        Arrays.equals(repoPassword, msgDigestSHA256(password).toCharArray()))
                .orElse(false);
    }


    public String msgDigestSHA256(char[] msg) {
        String hashMsg = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            byte[] hash = messageDigest.digest(charaTobytea(msg));
            hashMsg = HexFormat.of().formatHex(hash);
            Arrays.fill(hash, (byte) 0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashMsg;
    }

    private byte[] charaTobytea(char[] input) {
        byte[] out = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = (byte) input[i];
        }
        return out;
    }
}

