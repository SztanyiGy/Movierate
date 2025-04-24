package com.example.movierate.service;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.exception.MovieException;
import com.example.movierate.model.Moviemodel;
import com.example.movierate.model.Reviewmodel;
import com.example.movierate.repository.MovieRepository;
import com.example.movierate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements Reviewservice {

    private final ReviewRepository reviewrepository;
    private final MovieRepository movierepository;

    @Override
    public List<ReviewDto> getReviewsByMovieId(Long movieId) {
        Moviemodel movie = movierepository.findById(movieId)
                .orElseThrow(() -> new MovieException("Movie not found"));

        return movie.getReviews().stream()
                .map(r -> {
                    ReviewDto dto = new ReviewDto();
                    dto.setId(r.getId());
                    dto.setReviewerName(r.getReviewerName());
                    dto.setRating(r.getScore());
                    dto.setComment(r.getComment());
                    dto.setMovieId(movieId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto addReviewToMovie(Long movieId, ReviewDto reviewDto) {
        Moviemodel movie = movierepository.findById(movieId)
                .orElseThrow(() -> new MovieException("Movie not found"));

        Reviewmodel review = new Reviewmodel();

        // Automatikusan beállítjuk a bejelentkezett felhasználó nevét
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        review.setReviewerName(loggedInUsername);

        review.setScore(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setMovie(movie);

        review = reviewrepository.save(review);

        return new ReviewDto(
                review.getId(),
                review.getReviewerName(),
                review.getScore(),
                review.getComment(),
                movieId
        );
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        Reviewmodel review = reviewrepository.findById(id)
                .orElseThrow(() -> new MovieException("Review not found"));

        review.setReviewerName(reviewDto.getReviewerName()); // Ha update-nél is automatikus név kell, ezt is lehet módosítani.
        review.setScore(reviewDto.getRating());
        review.setComment(reviewDto.getComment());

        review = reviewrepository.save(review);

        return new ReviewDto(
                review.getId(),
                review.getReviewerName(),
                review.getScore(),
                review.getComment(),
                review.getMovie().getId()
        );
    }

    @Override
    public void deleteReview(Long id) {
        reviewrepository.deleteById(id);
    }

    public double calculateAverageRatingForMovie(Long movieId) {
        List<ReviewDto> reviews = getReviewsByMovieId(movieId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ReviewDto::getRating)
                .average()
                .orElse(0.0);
    }
}
