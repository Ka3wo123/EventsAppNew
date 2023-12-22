package pl.test.events.models.dto;

import org.springframework.stereotype.Service;
import pl.test.events.models.User;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        return new UserDto(user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword());
    }
}
