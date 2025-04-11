package com.example.movierate.service;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.exception.MovieException;
import com.example.movierate.model.Moviemodel;
import com.example.movierate.repository.Movierepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.movierate.dto.ReviewDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImp implements Movieservice {

    private final Movierepository movierepository;  // Kihasználva a @RequiredArgsConstructor

    @Override
    public List<MovieDto> getAllMovies() {
        return movierepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDto getMovieById(Long id) {
        Moviemodel movie = movierepository.findById(id).orElseThrow(() -> new MovieException("Movie not found"));
        return convertToDto(movie);
    }

    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        Moviemodel movie = new Moviemodel();
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setYear(movieDto.getYear());
        movie.setGenre(movieDto.getGenre());
        movie = movierepository.save(movie);
        return convertToDto(movie);
    }

    @Override
    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Moviemodel movie = movierepository.findById(id).orElseThrow(() -> new MovieException("Movie not found"));
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setYear(movieDto.getYear());
        movie.setGenre(movieDto.getGenre());
        movie = movierepository.save(movie);
        return convertToDto(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movierepository.deleteById(id);
    }

    private MovieDto convertToDto(Moviemodel movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDirector(movie.getDirector());
        movieDto.setYear(movie.getYear());
        movieDto.setGenre(movie.getGenre());
        movieDto.setReviews(movie.getReviews().stream()
                .map(review -> new ReviewDto(review.getId(), review.getReviewerName(),
                        review.getScore(), review.getComment(), review.getMovie().getId()))
                .collect(Collectors.toList()));
        return movieDto;
    }
}

