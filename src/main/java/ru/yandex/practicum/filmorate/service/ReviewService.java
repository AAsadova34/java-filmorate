package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dal.ReviewRatingStorage;
import ru.yandex.practicum.filmorate.storage.dal.ReviewStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewRatingStorage reviewRatingStorage;
    private final UserService userService;
    private final FilmService filmService;

    public Review addReview(Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        Review reviewInStorage = reviewStorage.addReview(review);
        Logger.logSave(HttpMethod.POST, "/reviews", reviewInStorage.toString());
        return reviewInStorage;
    }

    public Review updateReview(Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        Review reviewInStorage = reviewStorage.updateReview(review);
        Logger.logSave(HttpMethod.PUT, "/reviews", reviewInStorage.toString());
        return reviewInStorage;
    }

    public void removeReview(long id) {
        boolean removal = reviewStorage.removeReview(id);
        if (!removal) {
            throw new ObjectNotFoundException(String.format("Review with id %s not found", id));
        }
        Logger.logSave(HttpMethod.DELETE, "/reviews/" + id, ((Boolean) removal).toString());
    }

    public Review getReviewById(long id) {
        Logger.logSave(HttpMethod.GET, "/reviews/" + id, "no body");
        return reviewStorage.getReviewById(id);
    }

    public List<Review> getReviews(long filmId, int count) {
        List<Review> listReview = reviewStorage.getReviews(filmId, count);
        Logger.logSave(HttpMethod.GET, "/reviews?filmId=" + filmId + "&count=" + count, "no body");
        if (!listReview.isEmpty()) {
            return listReview.stream()
                    .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                    .collect(Collectors.toList());
        }
        return listReview;
    }

    public void likeReview(long reviewId, long userId) {
        boolean addition;
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewRatingStorage.removeLikeDislike(reviewId, userId, FALSE);
        addition = reviewRatingStorage.addLikeDislike(reviewId, userId, TRUE);
        Logger.logSave(HttpMethod.PUT, "/reviews/" + reviewId + "/like/" + userId, ((Boolean) addition).toString());
    }

    public void dislikeReview(long reviewId, long userId) {
        boolean addition;
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        reviewRatingStorage.removeLikeDislike(reviewId, userId, TRUE);
        addition = reviewRatingStorage.addLikeDislike(reviewId, userId, FALSE);
        Logger.logSave(HttpMethod.PUT, "/reviews/" + reviewId + "/dislike/" + userId,
                ((Boolean) addition).toString());
    }

    public void removeLikeReview(long reviewId, long userId) {
        boolean removal;
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        removal = reviewRatingStorage.removeLikeDislike(reviewId, userId, TRUE);
        if (!removal) {
            throw new ObjectNotFoundException(String.format("User with id %s did not like the review with id %s",
                    userId, reviewId));
        }
        Logger.logSave(HttpMethod.DELETE, "/reviews/" + reviewId + "/like/" + userId,
                ((Boolean) removal).toString());
    }

    public void removeDislikeReview(long reviewId, long userId) {
        boolean removal;
        reviewStorage.getReviewById(reviewId);
        userService.getUserById(userId);
        removal = reviewRatingStorage.removeLikeDislike(reviewId, userId, FALSE);
        if (!removal) {
            throw new ObjectNotFoundException(String.format("User with id %s did not dislike the review with id %s",
                    userId, reviewId));
        }
        Logger.logSave(HttpMethod.DELETE, "/reviews/" + reviewId + "/dislike/" + userId,
                ((Boolean) removal).toString());
    }

}