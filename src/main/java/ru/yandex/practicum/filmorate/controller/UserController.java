package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping//получить полный список пользователей
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping//добавить пользователя
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping//обновить пользователя или добавить пользователя (если он не был добавлен ранее)
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}") //получить пользователя по id
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")//добавить в друзья
    public List<Long> addAsFriend(@PathVariable long id,
                            @PathVariable long friendId) {
        return userService.addAsFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")//удалить из друзей
    public List<Long> removeFromFriends(@PathVariable long id,
                                  @PathVariable long friendId) {
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")//получить список друзей
    public List<User> getListOfFriends(@PathVariable long id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")//получить список общих друзей
    public List<User> getAListOfMutualFriends(@PathVariable long id,
                                              @PathVariable long otherId) {
        return userService.getAListOfMutualFriends(id, otherId);
    }
}
