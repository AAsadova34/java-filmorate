package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.HttpMethods;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final UserService userService;
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Autowired
    public InMemoryFilmStorage(UserService userService) {
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        logRequest(HttpMethods.GET, "/films", "no body");
        logSave(HttpMethods.GET, "/films", films.values().toString());
        return films.values();
    }

    public Film addFilm(Film film) {
        logRequest(HttpMethods.POST, "/films", film.toString());
        Film verifiedFilm = checkValidation(film);
        if (films.containsKey(verifiedFilm.getId())) {
            throw new RedoCreationException("Movie already exists");
        }
        films.put(verifiedFilm.getId(), verifiedFilm);
        logSave(HttpMethods.POST, "/films", verifiedFilm.toString());
        return film;
    }

    public Film updateFilm(Film film) {
        logRequest(HttpMethods.PUT, "/films", film.toString());
        filmExistenceCheck(film.getId());
        Film verifiedFilm = checkValidation(film);
        films.put(verifiedFilm.getId(), verifiedFilm);
        logSave(HttpMethods.PUT, "/films", verifiedFilm.toString());
        return film;
    }

    public Film getFilmById(long id) {
        logRequest(HttpMethods.GET, "/films/" + id, "no body");
        filmExistenceCheck(id);
        logSave(HttpMethods.GET, "/films/" + id, films.get(id).toString());
        return films.get(id);
    }

    public List<Long> addLike(long id, long userId) {
        logRequest(HttpMethods.PUT, "/films/" + id + "/like/" + userId, "no body");
        filmExistenceCheck(id);
        userService.getUserById(userId);
        films.get(id).getLikes().add(userId);
        logSave(HttpMethods.PUT, "/films/" + id + "/like/" + userId, films.get(id).toString());
        return List.of(id, userId);
    }

    public List<Long> unlike(long id, long userId) {
        logRequest(HttpMethods.DELETE, "/films/" + id + "/like/" + userId, "no body");
        filmExistenceCheck(id);
        userService.getUserById(userId);
        if (!films.get(id).getLikes().contains(userId)) {
            throw new ObjectNotFoundException(String.format("User with id %s did not like the movie with id %s",
                    userId, id));
        }
        films.get(id).getLikes().remove(userId);
        logSave(HttpMethods.DELETE, "/films/" + id + "/like/" + userId, films.get(id).toString());
        return List.of(id, userId);
    }

    public List<Film> getTheBestFilms(int count) {
        logRequest(HttpMethods.GET, "/films/popular?count=" + count, "no body");
        return films.values().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(), (f1, f2) -> f2 - f1))
                .limit(count)
                .collect(Collectors.toList());
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

    private void logRequest(HttpMethods method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }

    private void logSave(HttpMethods method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }

    private void filmExistenceCheck(long id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Film with id %s not found", id));
        }
    }
}
