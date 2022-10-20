package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilms() { return films.values(); }

    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new RedoCreationException("Movie already exists");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmExistenceCheck(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilmById(long id) {
        filmExistenceCheck(id);
        return films.get(id);
    }

    private void filmExistenceCheck(long id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Film with id %s not found", id));
        }
    }
}
