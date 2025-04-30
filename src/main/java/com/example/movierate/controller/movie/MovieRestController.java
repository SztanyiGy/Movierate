package com.example.movierate.controller.movie;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.service.Movieservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieRestController {

    private final Movieservice movieservice;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieservice.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieservice.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody
                                                    MovieDto movieDto) {
        return ResponseEntity.ok(movieservice.createMovie(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovieApi(
            @PathVariable Long id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieservice.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieservice.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
