package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(long id);

    List<Long> addAsFriend(long id, long friendId);

    List<Long> removeFromFriends(long id, long friendId);

    List<User> getListOfFriends(long id);

    List<User> getAListOfMutualFriends(long id, long otherId);
}
