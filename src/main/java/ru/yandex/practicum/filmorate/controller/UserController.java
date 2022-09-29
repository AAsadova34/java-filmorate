package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;


    @GetMapping
    public Collection getUsers() {
        logRequest("GET", "/users", "no body");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        logRequest("POST", "/users", user.toString());
        User verifiedUser = checkValidation(user);
        if (!users.containsKey(verifiedUser.getId())) {
            users.put(verifiedUser.getId(), verifiedUser);
        } else {
            throw new RedoCreationException("User already exists");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        logRequest("PUT", "/users", user.toString());
        User verifiedUser = checkValidation(user);
        users.put(verifiedUser.getId(), verifiedUser);
        return user;
    }

    private void generateId() {
        id++;
    }

    private User checkValidation(User user) {
        if (user.getId() == 0) {
            generateId();
            user.setId(id);
        }
        if (user.getId() < 0) {
            throw new ValidationException("id must not be negative");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login must not contain spaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private void logRequest(String method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
