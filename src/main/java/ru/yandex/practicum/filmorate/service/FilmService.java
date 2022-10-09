package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private long id = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        Collection<Film> filmsInStorage = filmStorage.getFilms();

        logSave(HttpMethod.GET, "/films", filmsInStorage.toString());
        return filmsInStorage;
    }

    public Film addFilm(Film film) {
        Film filmInStorage = filmStorage.addFilm(checkValidation(film));

        logSave(HttpMethod.POST, "/films", filmInStorage.toString());
        return filmInStorage;
    }

    public Film updateFilm(Film film) {
        Film filmInStorage = filmStorage.updateFilm(checkValidation(film));

        logSave(HttpMethod.PUT, "/films", filmInStorage.toString());
        return filmInStorage;
    }

    public Film getFilmById(long id) {
        Film filmInStorage = filmStorage.getFilmById(id);

        logSave(HttpMethod.GET, "/films/" + id, filmInStorage.toString());
        return filmInStorage;
    }

    public Film addLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        userService.getUserById(userId);
        film.getLikes().add(userId);

        logSave(HttpMethod.PUT, "/films/" + id + "/like/" + userId, film.toString());
        return film;
    }

    public Film unlike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        userService.getUserById(userId);
        if (!film.getLikes().contains(userId)) {
            throw new ObjectNotFoundException(String.format("User with id %s did not like the movie with id %s",
                    userId, id));
        }
        film.getLikes().remove(userId);

        logSave(HttpMethod.DELETE, "/films/" + id + "/like/" + userId, film.toString());
        return film;
    }

    public List<Film> getTheBestFilms(int count) {
        List<Film> bestFilms = filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(), (f1, f2) -> f2 - f1))
                .limit(count)
                .collect(Collectors.toList());

        logSave(HttpMethod.GET, "/films/popular?count=" + count, bestFilms.toString());
        return bestFilms;
    }

    private void generateId() {
        id++;
    }

    private Film checkValidation(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date must not be earlier than 12-28-1895");
        }
        if (film.getId() == 0) {
            generateId();
            film.setId(id);
        }
        return film;
    }

    private void logSave(HttpMethod method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }
}
