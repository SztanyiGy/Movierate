package com.example.movierate.controller.review;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final Reviewservice reviewservice;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovie(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewservice.getReviewsByMovieId(movieId);
        // Ha üres a lista, üres listát ad vissza, nem null-t
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/movie/{movieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long movieId,
                                               @RequestBody ReviewDto reviewDto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        reviewDto.setReviewerName(userDetails.getUsername());
        return ResponseEntity.ok(reviewservice.addReviewToMovie(movieId, reviewDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewservice.updateReview(id, reviewDto));
    }

    @DeleteMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, @RequestParam Long movieId) {
        reviewservice.deleteReview(id);  // Törli a véleményt
        return "redirect:/movies/" + movieId;  // Visszairányít a film adatlapjára
    }
}
