package com.example.movierate.service;

import com.example.movierate.dto.ReviewDto;

import java.util.List;

public interface Reviewservice {
    List<ReviewDto> getReviewsByMovieId(Long movieId);
    ReviewDto addReviewToMovie(Long movieId, ReviewDto reviewDto);
    ReviewDto updateReview(Long id, ReviewDto reviewDto);
    void deleteReview(Long id);
    double calculateAverageRatingForMovie(Long id);
    // Új metódus
    ReviewDto getReviewById(Long id);
}
