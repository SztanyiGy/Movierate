package com.example.movierate.controller;

import java.util.List;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.service.Movieservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller osztály, amely a filmekkel kapcsolatos HTTP kéréseket kezeli.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private Movieservice movieservice;

    // Frontend oldal megjelenítése
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieservice.getAllMovies());
        return "index"; // src/main/resources/templates/index.html
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
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieservice.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieservice.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}