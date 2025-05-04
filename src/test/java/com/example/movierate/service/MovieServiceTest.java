package com.example.movierate.service;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.exception.MovieException;
import com.example.movierate.model.Moviemodel;
import com.example.movierate.model.Reviewmodel;
import com.example.movierate.repository.MovieRepository;
import com.example.movierate.service.impl.MovieServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImp movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMovies() {
        Moviemodel movie = createDummyMovie();
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDto> result = movieService.getAllMovies();

        assertEquals(1, result.size());
        assertEquals("The Matrix", result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Found() {
        Moviemodel movie = createDummyMovie();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieDto result = movieService.getMovieById(1L);

        assertEquals("The Matrix", result.getTitle());
        assertEquals("Sci-Fi", result.getGenre());
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(MovieException.class, () -> movieService.getMovieById(999L));
    }

    @Test
    void testCreateMovie() {
        MovieDto dto = new MovieDto();
        dto.setTitle("Inception");
        dto.setDirector("Nolan");
        dto.setGenre("Sci-Fi");
        dto.setYear(2010);

        Moviemodel saved = new Moviemodel();
        saved.setId(1L);
        saved.setTitle("Inception");
        saved.setDirector("Nolan");
        saved.setGenre("Sci-Fi");
        saved.setYear(2010);

        when(movieRepository.save(any(Moviemodel.class))).thenReturn(saved);

        MovieDto result = movieService.createMovie(dto);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).save(any());
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(movieRepository).deleteById(1L);

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).deleteById(1L);
    }

    private Moviemodel createDummyMovie() {
        Moviemodel movie = new Moviemodel();
        movie.setId(1L);
        movie.setTitle("The Matrix");
        movie.setDirector("Wachowski");
        movie.setYear(1999);
        movie.setGenre("Sci-Fi");
        movie.setImg("matrix.jpg");
        movie.setActors("Keanu Reeves, Laurence Fishburne");
        movie.setDescription("A hacker discovers the truth about reality.");
        movie.setTrailer("https://youtube.com/matrix");

        Reviewmodel review = new Reviewmodel();
        review.setId(101L);
        review.setReviewerName("John");
        review.setScore(5);
        review.setComment("Excellent");
        review.setMovie(movie);

        movie.setReviews(List.of(review));
        return movie;
    }
}
