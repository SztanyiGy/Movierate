package com.example.movierate.controller;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ReviewWebController {

    private final Reviewservice reviewservice;

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        try {
            // Lekérjük a vélemény adatait, hogy megkapjuk a film ID-t
            ReviewDto review = reviewservice.getReviewById(id);
            Long movieId = review.getMovieId();

            // Töröljük a véleményt
            reviewservice.deleteReview(id);

            // Visszairányítunk a film részletes adatlapjára
            return "redirect:/movies/details/" + movieId;
        } catch (Exception e) {
            // Ha hiba van, visszairányítunk a főoldalra
            return "redirect:/movies/";
        }
    }
}