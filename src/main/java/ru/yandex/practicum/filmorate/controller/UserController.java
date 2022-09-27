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

import static ru.yandex.practicum.filmorate.exception.ErrorHandler.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    private void generateId() {
        id++;
    }
    @GetMapping
    public Collection getUsers() {
        logRequest("GET", "/users", "no body");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        logRequest("POST", "/users", user.toString());

        if (user.getId() == 0) {
            generateId();
            user.setId(id);
        }
        if (user.getLogin().contains(" ")) {
            ValidationException e = new ValidationException("Login must not contain spaces");
            handleValidationException(e);
            throw e;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            RedoCreationException e = new RedoCreationException("User already exists");
            handleRedoCreationException(e);
            throw e;
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        logRequest("PUT", "/users", user.toString());

        if (user.getId() == 0) {
            generateId();
            user.setId(id);
        }
        if (user.getLogin().contains(" ")) {
            ValidationException e = new ValidationException("Login must not contain spaces");
            handleValidationException(e);
            throw e;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    private void logRequest(String method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
