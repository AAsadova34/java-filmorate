package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) { return filmStorage.updateFilm(film); }

    public Film getFilmById(long id) { return filmStorage.getFilmById(id); }

    public List<Long> addLike(long id, long userId) {
        return filmStorage.addLike(id, userId);
    }

    public List<Long> unlike(long id, long userId) {
        return filmStorage.unlike(id, userId);
    }

    public List<Film> getTheBestFilms(int count) {
        return filmStorage.getTheBestFilms(count);
    }
}
