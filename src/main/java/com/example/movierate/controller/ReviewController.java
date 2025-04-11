package com.example.movierate.controller;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final Reviewservice reviewservice;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewservice.getReviewsByMovieId(movieId));
    }

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long movieId, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewservice.addReviewToMovie(movieId, reviewDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewservice.updateReview(id, reviewDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewservice.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
