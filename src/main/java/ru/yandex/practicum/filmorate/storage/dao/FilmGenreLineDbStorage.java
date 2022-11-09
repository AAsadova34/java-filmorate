package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenreLine;
import ru.yandex.practicum.filmorate.storage.dal.FilmGenreLineStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmGenreLineDbStorage implements FilmGenreLineStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addGenre(int genreId, long filmId) {
        FilmGenreLine filmGenreLine = FilmGenreLine.builder()
                .genreId(genreId)
                .filmId(filmId).
                build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_genre_line");
        return simpleJdbcInsert.execute(toMap(filmGenreLine)) > 0;
    }

    @Override
    public boolean deleteGenre(long filmId) {
        String sqlQuery = "delete from film_genre_line where FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public List<Integer> getListOfGenres(long id) {
        String sqlQuery = "select GENRE_ID from FILM_GENRE_LINE where FILM_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id);
    }

    public Map<String, Object> toMap(FilmGenreLine filmGenreLine) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", filmGenreLine.getFilmId());
        values.put("genre_id", filmGenreLine.getGenreId());
        return values;
    }
}
