package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) { return userStorage.updateUser(user); }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public List<Long> addAsFriend(long id, long friendId) {
        return userStorage.addAsFriend(id, friendId);
    }

    public List<Long> removeFromFriends(long id, long friendId) {
        return userStorage.removeFromFriends(id, friendId);
    }

    public List<User> getListOfFriends(long id) {
        return userStorage.getListOfFriends(id);
    }

    public List<User> getAListOfMutualFriends(long id, long otherId) {
        return userStorage.getAListOfMutualFriends(id, otherId);
    }


}
