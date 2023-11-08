package ru.practicum.evm.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.user.NewUserRequest;
import ru.practicum.evm.dto.user.UserDto;
import ru.practicum.evm.model.User;

@Service
public class UserMapper {
    public User toUser(NewUserRequest newUserRequest) {
        return User.builder().email(newUserRequest.getEmail()).name(newUserRequest.getName()).build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder().id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
