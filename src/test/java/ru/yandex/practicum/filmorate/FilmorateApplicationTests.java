package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    UserController userController;
    FilmController filmController;

    @Autowired
    public FilmorateApplicationTests(UserController userController, FilmController filmController) {
        this.userController = userController;
        this.filmController = filmController;
    }

    @Test
    public void addUserWithoutIdTest() {
        User user = User.builder()
                .email("user1@yandex.ru")
                .login("user1")
                .name("User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertNotEquals(0, userController.addUser(user).getId(), "Не сгенерирован id");
    }

    @Test
    public void addUseAgainTest() {
        User user1 = User.builder()
                .email("user2@yandex.ru")
                .login("user2")
                .name("User2")
                .birthday(LocalDate.of(1990, 1, 2))
                .build();
        userController.addUser(user1);
        User user2 = User.builder()
                .id(user1.getId())
                .email("newUser2@yandex.ru")
                .login("newUser2")
                .name("NewUser2")
                .birthday(LocalDate.of(1990, 1, 2))
                .build();

        RedoCreationException e = Assertions.assertThrows(
                RedoCreationException.class, () -> userController.addUser(user2));
        assertEquals("User already exists", e.getMessage());
    }

    @Test
    public void addUserWithInvalidLoginFormatTest() {
        User user = User.builder()
                .email("user3@yandex.ru")
                .login("user 3")
                .name("User3")
                .birthday(LocalDate.of(1990, 1, 3))
                .build();
        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> userController.addUser(user));

        assertEquals("Login must not contain spaces",
                e.getMessage());
    }

    @Test
    public void addUserWithNullNameTest() {
        User user = User.builder()
                .email("user4@yandex.ru")
                .login("user4")
                .birthday(LocalDate.of(1990, 1, 4))
                .build();
        assertEquals("user4", userController.addUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void addUserWithEmptyNameTest() {
        User user = User.builder()
                .email("user5@yandex.ru")
                .login("user5")
                .name(" ")
                .birthday(LocalDate.of(1990, 1, 5))
                .build();
        assertEquals("user5", userController.addUser(user).getName(),
                "login и name не совпадают");
    }

    @Test
    public void addUserWithNullBirthdayTest() {
        User user = User.builder()
                .email("user6@yandex.ru")
                .login("user6")
                .name("User6")
                .build();

        assertNull(userController.addUser(user).getBirthday(),
                "Не создан пользователь без даты рождения");
    }

    @Test
    public void updateUserTest() {
        User user1 = User.builder()
                .email("user7@yandex.ru")
                .login("user7")
                .name("User7")
                .birthday(LocalDate.of(1990, 1, 7))
                .build();
        userController.addUser(user1);
        User user2 = User.builder()
                .id(user1.getId())
                .email("newUser7@yandex.ru")
                .login("newUser7")
                .name("NewUser7")
                .birthday(LocalDate.of(1990, 1, 7))
                .build();
        userController.updateUser(user2);
        assertFalse(userController.getUsers().contains(user1), "Пользователь не обновлен");
        assertTrue(userController.getUsers().contains(user2), "Пользователь не обновлен");
    }

    @Test
    public void updateAnUncreatedUserTest() {
        User user1 = User.builder()
                .email("user8@yandex.ru")
                .login("user8")
                .name("User8")
                .birthday(LocalDate.of(1990, 1, 8))
                .build();
        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> userController.updateUser(user1));
        assertEquals("User with id " + user1.getId() + " not found", e.getMessage());
    }

    @Test
    public void addFilmWithoutIdTest() {
        Film film = Film.builder()
                .name("Psycho1")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();

        assertNotEquals(0, filmController.addFilm(film).getId(), "Не сгенерирован id");
    }

    @Test
    public void addFilmAgainTest() {
        Film film1 = Film.builder()
                .name("Psycho2")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film1);
        Film film2 = Film.builder()
                .id(film1.getId())
                .name("Psiconautas2")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();

        RedoCreationException e = Assertions.assertThrows(
                RedoCreationException.class, () -> filmController.addFilm(film2));
        assertEquals("Movie already exists", e.getMessage());
    }

    @Test
    public void addAFilmWithNullDescriptionTest() {
        Film film = Film.builder()
                .name("Psycho3")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без описания");
    }

    @Test
    public void addFilmWithAnIncorrectReleaseDateTest() {
        Film film = Film.builder()
                .name("Psycho4")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(109)
                .build();

        ValidationException e = Assertions.assertThrows(
                ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Release date must not be earlier than 12-28-1895", e.getMessage());
    }

    @Test
    public void addFilmWithNullReleaseDateTest() {
        Film film = Film.builder()
                .name("Psycho5")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без даты релиза");
    }

    @Test
    public void updateFilmTest() {
        Film film1 = Film.builder()
                .name("Psycho6")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film1);
        Film film2 = Film.builder()
                .id(film1.getId())
                .name("Psiconautas6")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.updateFilm(film2);

        assertFalse(filmController.getFilms().contains(film1), "Фильм не обновлен");
        assertTrue(filmController.getFilms().contains(film2), "Фильм не обновлен");
    }

    @Test
    public void updateAnUncreatedFilmTest() {
        Film film1 = Film.builder()
                .name("Psycho7")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> filmController.updateFilm(film1));
        assertEquals("Film with id " + film1.getId() + " not found", e.getMessage());
    }
}