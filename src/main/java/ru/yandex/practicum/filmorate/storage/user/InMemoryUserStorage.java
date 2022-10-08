package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.HttpMethods;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;


    public Collection<User> getUsers() {
        logRequest(HttpMethods.GET, "/users", "no body");
        logSave(HttpMethods.GET, "/users", users.values().toString());
        return users.values();
    }

    public User addUser(User user) {
        logRequest(HttpMethods.POST, "/users", user.toString());
        User verifiedUser = checkValidation(user);
        if (users.containsKey(verifiedUser.getId())) {
            throw new RedoCreationException("User already exists");
        }
        users.put(verifiedUser.getId(), verifiedUser);
        logSave(HttpMethods.POST, "/users", verifiedUser.toString());
        return user;
    }

    public User updateUser(User user) {
        logRequest(HttpMethods.PUT, "/users", user.toString());
        userExistenceCheck(user.getId());
        User verifiedUser = checkValidation(user);
        users.put(verifiedUser.getId(), verifiedUser);
        logSave(HttpMethods.PUT, "/users", verifiedUser.toString());
        return user;
    }

    public User getUserById(long id) {
        logRequest(HttpMethods.GET, "/users/" + id, "no body");
        userExistenceCheck(id);
        logSave(HttpMethods.GET, "/users/" + id, users.get(id).toString());
        return users.get(id);
    }

    public List<Long> addAsFriend(long id, long friendId) {
        logRequest(HttpMethods.PUT, "/users/" + id + "/friends/" + friendId, "no body");
        userExistenceCheck(id);
        userExistenceCheck(friendId);
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        logSave(HttpMethods.PUT, "/users/" + id + "/friends/",
                List.of(users.get(id), users.get(friendId)).toString());
        return List.of(id, friendId);
    }

    public List<Long> removeFromFriends(long id, long friendId) {
        logRequest(HttpMethods.DELETE, "/users/" + id + "/friends/" + friendId, "no body");
        userExistenceCheck(id);
        userExistenceCheck(friendId);
        if (!users.get(id).getFriends().contains(friendId)) {
            throw new ObjectNotFoundException(String.format("User with id %s is not friends with user with id %s",
                    friendId, id));
        }
        if (!users.get(friendId).getFriends().contains(id)) {
            throw new ObjectNotFoundException(String.format("User with id %s is not friends with user with id %s",
                    id, friendId));
        }
        users.get(id).getFriends().remove(id);
        users.get(friendId).getFriends().remove(id);
        logSave(HttpMethods.DELETE, "/users/" + id + "/friends/" + friendId,
                List.of(users.get(id), users.get(friendId)).toString());
        return List.of(id, friendId);
    }

    public List<User> getListOfFriends(long id) {
        logRequest(HttpMethods.GET, "/users/" + id + "/friends", "no body");
        userExistenceCheck(id);
        Set<Long> friends = users.get(id).getFriends();
            return users.values().stream()
                    .filter(user -> friends.contains(user.getId()))
                    .collect(Collectors.toList());
    }

    public List<User> getAListOfMutualFriends(long id, long otherId) {
        logRequest(HttpMethods.GET, "/users/" + id + "/friends/common/" + otherId, "no body");
        userExistenceCheck(id);
        userExistenceCheck(otherId);
        Set<Long> friends = users.get(id).getFriends();
        Set<Long> otherFriends = users.get(otherId).getFriends();
            return users.values().stream()
                    .filter(user -> friends.contains(user.getId()))
                    .filter(user -> otherFriends.contains(user.getId()))
                    .collect(Collectors.toList());
    }

    private void generateId() {
        id++;
    }

    private User checkValidation(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login must not contain spaces");
        }
        if (user.getId() == 0) {
            generateId();
            user.setId(id);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private void logRequest(HttpMethods method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }

    private void logSave(HttpMethods method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }

    private void userExistenceCheck(long id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException(String.format("User with id %s not found", id));
        }
    }
}
