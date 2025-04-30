package com.example.movierate.controller.review;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    @GetMapping("/reviews/edit/{id}")
    public String editReviewForm(@PathVariable Long id, Model model) {
        try {
            // Lekérjük a vélemény adatait
            ReviewDto review = reviewservice.getReviewById(id);
            model.addAttribute("review", review);
            return "edit";
        } catch (Exception e) {
            // Ha hiba van, visszairányítunk a főoldalra
            return "redirect:/movies/";
        }
    }

    @PostMapping("/reviews/update/{id}")
    public String updateReview(@PathVariable Long id,
                               @ModelAttribute ReviewDto reviewDto) {
        try {
            // Frissítjük a véleményt
            ReviewDto updatedReview = reviewservice.updateReview(id, reviewDto);
            // Visszairányítunk a film részletes adatlapjára
            return "redirect:/movies/details/" + updatedReview.getMovieId();
        } catch (Exception e) {
            // Ha hiba van, visszairányítunk a főoldalra
            return "redirect:/movies/";
        }
    }
}
