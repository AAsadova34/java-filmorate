package ru.yandex.practicum.filmorate.storage.dal;

import java.util.List;

public interface FilmGenreLineStorage {

    boolean addGenre(int genreId, long filmId);

    boolean deleteGenre(long filmId);
    List<Integer> getListOfGenres(long id);

}
