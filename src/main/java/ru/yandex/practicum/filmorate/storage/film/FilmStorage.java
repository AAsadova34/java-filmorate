package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    List<Long> addLike(long id, long userId);

    List<Long> unlike(long id, long userId);

    List<Film> getTheBestFilms(int count);
}
