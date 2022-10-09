package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private long id = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        Collection<User> usersInStorage = userStorage.getUsers();

        logSave(HttpMethod.GET, "/users", usersInStorage.toString());
        return usersInStorage;
    }

    public User addUser(User user) {
        User userInStorage = userStorage.addUser(checkValidation(user));

        logSave(HttpMethod.POST, "/users", userInStorage.toString());
        return userInStorage;
    }

    public User updateUser(User user) {
        User userInStorage = userStorage.updateUser(checkValidation(user));

        logSave(HttpMethod.PUT, "/users", userInStorage.toString());
        return userInStorage;
    }

    public User getUserById(long id) {
        User userInStorage = userStorage.getUserById(id);

        logSave(HttpMethod.GET, "/users/" + id, userInStorage.toString());
        return userInStorage;
    }

    public List<User> addAsFriend(long id, long friendId) {
        userStorage.getUserById(id).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(id);
        List<User> friends = List.of(userStorage.getUserById(id), userStorage.getUserById(friendId));

        logSave(HttpMethod.PUT, "/users/" + id + "/friends/", friends.toString());
        return friends;
    }

    public List<User> removeFromFriends(long id, long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (!user.getFriends().contains(friendId)) {
            throw new ObjectNotFoundException(String.format("User with id %s is not friends with user with id %s",
                    friendId, id));
        }
        if (!friend.getFriends().contains(id)) {
            throw new ObjectNotFoundException(String.format("User with id %s is not friends with user with id %s",
                    id, friendId));
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        List<User> formerFriends = List.of(user, friend);

        logSave(HttpMethod.DELETE, "/users/" + id + "/friends/" + friendId, formerFriends.toString());
        return formerFriends;
    }

    public List<User> getListOfFriends(long id) {
        Set<Long> friends = userStorage.getUserById(id).getFriends();
        List<User> friendList = userStorage.getUsers().stream()
                .filter(user -> friends.contains(user.getId()))
                .collect(Collectors.toList());

        logSave(HttpMethod.GET, "/users/" + id + "/friends", friendList.toString());
        return friendList;
    }

    public List<User> getAListOfMutualFriends(long id, long otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        Set<Long> friends = user.getFriends();
        Set<Long> otherFriends = otherUser.getFriends();
        List<User> mutualFriends = userStorage.getUsers().stream()
                .filter(u -> friends.contains(u.getId()))
                .filter(u -> otherFriends.contains(u.getId()))
                .collect(Collectors.toList());

        logSave(HttpMethod.GET, "/users/" + id + "/friends/common/" + otherId, mutualFriends.toString());
        return mutualFriends;
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

    private void logSave(HttpMethod method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }

}
