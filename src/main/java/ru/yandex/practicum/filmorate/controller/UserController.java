package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping//получить полный список пользователей
    public Collection<User> getUsers() {
        logRequest(HttpMethod.GET, "/users", "no body");
        return userService.getUsers();
    }

    @PostMapping//добавить пользователя
    public User addUser(@Valid @RequestBody User user) {
        logRequest(HttpMethod.POST, "/users", user.toString());
        return userService.addUser(user);
    }

    @PutMapping//обновить пользователя или добавить пользователя (если он не был добавлен ранее)
    public User updateUser(@Valid @RequestBody User user) {
        logRequest(HttpMethod.PUT, "/users", user.toString());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}") //получить пользователя по id
    public User getUserById(@PathVariable long id) {
        logRequest(HttpMethod.GET, "/users/" + id, "no body");
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")//добавить в друзья
    public List<User> addAsFriend(@PathVariable long id,
                            @PathVariable long friendId) {
        logRequest(HttpMethod.PUT, "/users/" + id + "/friends/" + friendId, "no body");
        return userService.addAsFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")//удалить из друзей
    public List<User> removeFromFriends(@PathVariable long id,
                                  @PathVariable long friendId) {
        logRequest(HttpMethod.DELETE, "/users/" + id + "/friends/" + friendId, "no body");
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")//получить список друзей
    public List<User> getListOfFriends(@PathVariable long id) {
        logRequest(HttpMethod.GET, "/users/" + id + "/friends", "no body");
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")//получить список общих друзей
    public List<User> getAListOfMutualFriends(@PathVariable long id,
                                              @PathVariable long otherId) {
        logRequest(HttpMethod.GET, "/users/" + id + "/friends/common/" + otherId, "no body");
        return userService.getAListOfMutualFriends(id, otherId);
    }

    private void logRequest(HttpMethod method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }
}
