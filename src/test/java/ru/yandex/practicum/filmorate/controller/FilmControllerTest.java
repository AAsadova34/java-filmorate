package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController(
            new FilmService(
                    new InMemoryFilmStorage(
                            new UserService(
                                    new InMemoryUserStorage()))));

    @Test
    public void addFilmWithoutIdTest() {
        Film film = Film.builder()
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();

        assertEquals(1, filmController.addFilm(film).getId(), "Не сгенерирован id");
    }

    @Test
    public void addFilmAgainTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        Film film2 = Film.builder()
                .id(1)
                .name("Psiconautas")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film1);

        RedoCreationException e = Assertions.assertThrows(
                RedoCreationException.class, () -> filmController.addFilm(film2));
        assertEquals("Movie already exists", e.getMessage());
    }

    @Test
    public void addAFilmWithNullDescriptionTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без описания");
    }

    @Test
    public void addFilmWithAnIncorrectReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("Psycho")
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
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .duration(109)
                .build();
        filmController.addFilm(film);
        assertTrue(filmController.getFilms().contains(film), "Не добавлен фильм без даты релиза");
    }

    @Test
    public void updateFilmTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        Film film2 = Film.builder()
                .id(1)
                .name("Psiconautas")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        filmController.addFilm(film1);
        filmController.updateFilm(film2);

        assertFalse(filmController.getFilms().contains(film1), "Фильм не обновлен");
        assertTrue(filmController.getFilms().contains(film2), "Фильм не обновлен");
    }

    @Test
    public void updateAnUncreatedFilmTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Psycho")
                .description("Американский психологический хоррор 1960 года, снятый режиссёром Альфредом Хичкоком.")
                .releaseDate(LocalDate.of(1960, 1, 1))
                .duration(109)
                .build();
        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> filmController.updateFilm(film1));
        assertEquals("Film with id 1 not found", e.getMessage());
    }
}