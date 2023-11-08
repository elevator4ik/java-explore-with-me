package ru.practicum.evm.service;

import ru.practicum.evm.dto.user.NewUserRequest;
import ru.practicum.evm.dto.user.UserDto;
import ru.practicum.evm.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUserById(Long userId);
}
