package com.example.movierate.controller.movie;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Movieservice;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final Movieservice movieservice;
    private final Reviewservice reviewservice;

    @GetMapping({"", "/"})  // Mind a "/movies", mind a "/movies/" útvonalat kezeli
    public String home(Model model) {
        model.addAttribute("movies", movieservice.getAllMovies());
        return "index";
    }

    @GetMapping("/new")
    public String newMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "new_movie";
    }

    @PostMapping("/new")
    public String createNewMovie(@ModelAttribute MovieDto movieDto) {
        movieservice.createMovie(movieDto);
        return "redirect:/movies/";
    }

    @GetMapping("/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieservice.getMovieById(id);
        model.addAttribute("movie", movieDto);
        return "edit_movie";
    }

    @PostMapping("/edit/{id}")
    public String updateMovieFromForm(@PathVariable Long id, @ModelAttribute MovieDto movieDto) {
        movieservice.updateMovie(id, movieDto);
        return "redirect:/movies/";
    }

    @PostMapping("/{id}/delete")
    public String deleteMovieViaForm(@PathVariable Long id) {
        movieservice.deleteMovie(id);
        return "redirect:/movies/";
    }

    @GetMapping("/{id}/reviews/new")
    public String showReviewForm(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieservice.getMovieById(id);
        model.addAttribute("movie", movieDto);
        model.addAttribute("review", new ReviewDto());
        return "new_review";
    }

    @PostMapping("/{id}/reviews")
    public String addReviewToMovie(@PathVariable Long id, @ModelAttribute ReviewDto reviewDto) {
        reviewservice.addReviewToMovie(id, reviewDto);
        return "redirect:/movies/";
    }

    @GetMapping("/details/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        MovieDto movie = movieservice.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("averageRating", reviewservice.calculateAverageRatingForMovie(id));
        return "movie_details";
    }
}
