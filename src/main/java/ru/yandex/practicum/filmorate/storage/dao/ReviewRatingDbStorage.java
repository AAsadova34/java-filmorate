package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewRating;
import ru.yandex.practicum.filmorate.storage.dal.ReviewRatingStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class ReviewRatingDbStorage implements ReviewRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLikeDislike(long reviewId, long userId, boolean isUseful) {
        ReviewRating reviewRating = ReviewRating.builder()
                .reviewId(reviewId)
                .userId(userId)
                .isUseful(isUseful)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review_rating");
        return simpleJdbcInsert.execute(toMap(reviewRating)) > 0;
    }

    @Override
    public boolean removeLikeDislike(long reviewId, long userId, boolean isUseful) {
        String sqlQuery = "delete from REVIEW_RATING where REVIEW_ID = ? and USER_ID = ? and IS_USEFUL = ?";
        return jdbcTemplate.update(sqlQuery, reviewId, userId, isUseful) > 0;
    }

    @Override
    public int getReviewRating(long reviewId) {
        String sqlQuery = "select COUNT(REVIEW_ID) " +
                "from REVIEW_RATING " +
                "where REVIEW_ID = ? and IS_USEFUL = ? " +
                "group by IS_USEFUL, REVIEW_ID";
        List<Integer> best = jdbcTemplate.queryForList(sqlQuery, Integer.class, reviewId, TRUE);
        List<Integer> worst = jdbcTemplate.queryForList(sqlQuery, Integer.class, reviewId, FALSE);
        if (!best.isEmpty() && !worst.isEmpty()) {
            return best.get(0) - worst.get(0);
        } else if (!best.isEmpty() && worst.isEmpty()) {
            return best.get(0);
        } else if (best.isEmpty() && !worst.isEmpty()) {
            return -(worst.get(0));
        } else {
            return 0;
        }
    }

    private Map<String, Object> toMap(ReviewRating reviewRating) {
        Map<String, Object> values = new HashMap<>();
        values.put("review_id", reviewRating.getReviewId());
        values.put("user_id", reviewRating.getUserId());
        values.put("is_useful", reviewRating.getIsUseful());
        return values;
    }
}
