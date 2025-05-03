package com.example.movierate.controllers;

import com.example.movierate.controller.movie.MovieController;
import com.example.movierate.dto.MovieDto;
import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Movieservice;
import com.example.movierate.service.Reviewservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private Movieservice movieservice;

    @Mock
    private Reviewservice reviewservice;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void home_ShouldReturnIndexView() throws Exception {
        // Arrange
        List<MovieDto> movies = new ArrayList<>();
        when(movieservice.getAllMovies()).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("movies", movies));

        verify(movieservice, times(1)).getAllMovies();
    }

    @Test
    void newMovieForm_ShouldReturnNewMovieView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/movies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_movie"))
                .andExpect(model().attributeExists("movie"));
    }

    @Test
    void createNewMovie_ShouldRedirectToMoviesPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/movies/new")
                        .param("title", "Test Movie")
                        .param("director", "Test Director")
                        .param("year", "2023"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/"));

        verify(movieservice, times(1)).createMovie(any(MovieDto.class));
    }

    @Test
    void editMovieForm_ShouldReturnEditMovieView() throws Exception {
        // Arrange
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1L);
        movieDto.setTitle("Test Movie");
        when(movieservice.getMovieById(1L)).thenReturn(movieDto);

        // Act & Assert
        mockMvc.perform(get("/movies/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_movie"))
                .andExpect(model().attributeExists("movie"));

        verify(movieservice, times(1)).getMovieById(1L);
    }

    @Test
    void updateMovieFromForm_ShouldRedirectToMoviesPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/movies/edit/1")
                        .param("title", "Updated Movie")
                        .param("director", "Updated Director")
                        .param("year", "2024"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/"));

        verify(movieservice, times(1)).updateMovie(eq(1L), any(MovieDto.class));
    }

    @Test
    void deleteMovieViaForm_ShouldRedirectToMoviesPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/movies/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/"));

        verify(movieservice, times(1)).deleteMovie(1L);
    }

    @Test
    void showReviewForm_ShouldReturnNewReviewView() throws Exception {
        // Arrange
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1L);
        movieDto.setTitle("Test Movie");
        when(movieservice.getMovieById(1L)).thenReturn(movieDto);

        // Act & Assert
        mockMvc.perform(get("/movies/1/reviews/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_review"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("review"));

        verify(movieservice, times(1)).getMovieById(1L);
    }

    @Test
    void addReviewToMovie_ShouldRedirectToMoviesPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/movies/1/reviews")
                        .param("reviewerName", "Test Reviewer")
                        .param("rating", "8")
                        .param("comment", "Great movie!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/"));

        verify(reviewservice, times(1)).addReviewToMovie(eq(1L), any(ReviewDto.class));
    }

    @Test
    void movieDetails_ShouldReturnMovieDetailsView() throws Exception {
        // Arrange
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1L);
        movieDto.setTitle("Test Movie");
        when(movieservice.getMovieById(1L)).thenReturn(movieDto);
        when(reviewservice.calculateAverageRatingForMovie(1L)).thenReturn(8.5);

        // Act & Assert
        mockMvc.perform(get("/movies/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie_details"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("averageRating"));

        verify(movieservice, times(1)).getMovieById(1L);
        verify(reviewservice, times(1)).calculateAverageRatingForMovie(1L);
    }
}