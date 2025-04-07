package com.example.movierate.service;

import com.example.movierate.dto.MovieDto;

import java.util.List;

public interface Movieservice {
    List<MovieDto> getAllMovies();
    MovieDto getMovieById(Long id);
    MovieDto createMovie(MovieDto movieDto);
    MovieDto updateMovie(Long id, MovieDto movieDto);
    void deleteMovie(Long id);
}
