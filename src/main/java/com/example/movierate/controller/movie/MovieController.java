package com.example.movierate.controller.movie;

import java.util.List;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Movieservice;
import com.example.movierate.service.Reviewservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller osztály, amely a filmekkel kapcsolatos HTTP kéréseket kezeli.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final Movieservice movieservice;
    private final Reviewservice reviewservice;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieservice.getAllMovies());
        return "index";
    }

    // Film hozzáadásának kezelése (GET)
    @GetMapping("/new")
    public String newMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto()); // Üres MovieDto-t adunk a formhoz
        return "new_movie"; // A form oldalt rendereljük
    }

    // Film hozzáadásának kezelése (POST)
    @PostMapping("/new")
    public String createNewMovie(@ModelAttribute MovieDto movieDto) {
        movieservice.createMovie(movieDto); // Hozzáadjuk a filmet
        return "redirect:/movies/"; // Visszairányítunk a filmek listájára
    }

    // Film módosítása (GET - form megjelenítése)
    @GetMapping("/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieservice.getMovieById(id);
        model.addAttribute("movie", movieDto);
        return "edit_movie";
    }

    // HTML form feldolgozás (update)
    @PostMapping("/edit/{id}")
    public String updateMovieFromForm(@PathVariable Long id, @ModelAttribute MovieDto movieDto) {
        movieservice.updateMovie(id, movieDto);
        return "redirect:/movies/";
    }

    // Film törlése HTML formból
    @PostMapping("/{id}/delete")
    public String deleteMovieViaForm(@PathVariable Long id) {
        movieservice.deleteMovie(id);
        return "redirect:/movies/";
    }

    // ⬇️ ÚJ: Vélemény hozzáadása űrlap megjelenítése
    @GetMapping("/{id}/reviews/new")
    public String showReviewForm(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieservice.getMovieById(id);
        model.addAttribute("movie", movieDto);
        model.addAttribute("review", new ReviewDto()); // üres review objektum a formhoz
        return "new_review"; // new_review.html sablont tölti be
    }

    @PostMapping("/{id}/reviews")
    public String addReviewToMovie(@PathVariable Long id, @ModelAttribute ReviewDto reviewDto) {
        reviewservice.addReviewToMovie(id, reviewDto);
        return "redirect:/movies/"; // vagy vissza az adott filmhez, ha van olyan oldalad
    }

    // REST API végpontok
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieservice.getAllMovies());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieservice.getMovieById(id));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieservice.createMovie(movieDto));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MovieDto> updateMovieApi(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieservice.updateMovie(id, movieDto));
    }
    @GetMapping("/details/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        MovieDto movie = movieservice.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("averageRating", reviewservice.calculateAverageRatingForMovie(id));
        return "movie_details"; // vagy amit használsz
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieservice.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
