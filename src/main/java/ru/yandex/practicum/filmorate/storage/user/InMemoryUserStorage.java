package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() { return users.values(); }

    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new RedoCreationException("User already exists");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        userExistenceCheck(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    public User getUserById(long id) {
        userExistenceCheck(id);
        return users.get(id);
    }

    private void userExistenceCheck(long id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException(String.format("User with id %s not found", id));
        }
    }
}
