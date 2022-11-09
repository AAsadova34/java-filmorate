package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.LikesStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesStorage likesStorage;

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

    public boolean addLike(long id, long userId) {
        boolean addition;
        filmStorage.getFilmById(id);
        userService.getUserById(userId);
        addition = likesStorage.addLike(id, userId);

        logSave(HttpMethod.PUT, "/films/" + id + "/like/" + userId, ((Boolean) addition).toString());
        return addition;
    }

    public boolean unlike(long id, long userId) {
        boolean removal;
        filmStorage.getFilmById(id);
        userService.getUserById(userId);
        removal = likesStorage.unlike(id, userId);
        if (!removal) {
            throw new ObjectNotFoundException(String.format("User with id %s did not like the movie with id %s",
                    userId, id));
        }

        logSave(HttpMethod.DELETE, "/films/" + id + "/like/" + userId, ((Boolean) removal).toString());
        return removal;
    }

    public List<Long> getListOfLikes(long id) {
        filmStorage.getFilmById(id);
        List<Long> likeList = likesStorage.getListOfLikes(id);

        logSave(HttpMethod.GET, "/films/" + id + "/likes", likeList.toString());
        return likeList;
    }

    public List<Film> getTheBestFilms(int count) {
        List<Film> bestFilms = likesStorage.getTheBestFilms(count).stream()
                .map(filmStorage::getFilmById)
                .collect(Collectors.toList());

        logSave(HttpMethod.GET, "/films/popular?count=" + count, bestFilms.toString());
        return bestFilms;
    }

    private Film checkValidation(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date must not be earlier than 12-28-1895");
        }
        return film;
    }

    private void logSave(HttpMethod method, String uri, String storage) {
        log.info("Endpoint request result: '{} {}'. In storage: '{}'", method, uri, storage);
    }
}
