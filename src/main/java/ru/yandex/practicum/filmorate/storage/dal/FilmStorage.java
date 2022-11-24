package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long filmId);

    List<Film> getListOfDirectorFilms(long directorId);

    boolean removeFilmById(long id);

    List<Film> getListOfFilmsByKeyword(String titleKeyWord, String directorKeyword);
}
