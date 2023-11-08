package ru.practicum.evm.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    public void checkUserExists(Long userId) throws IncorrectObjectException {
        if (!userRepository.existsById(userId)) {
            throw new IncorrectObjectException("There is no user with id = " + userId);
        }
    }
}
