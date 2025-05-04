package com.example.movierate.controllers;

import com.example.movierate.controller.review.ReviewWebController;
import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReviewWebControllerTest {

    @Mock
    private Reviewservice reviewService;

    @Mock
    private Model model;

    @InjectMocks
    private ReviewWebController reviewWebController;

    private ReviewDto reviewDto;
    private Long reviewId;
    private Long movieId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tesztadatok inicializálása
        reviewId = 1L;
        movieId = 2L;

        reviewDto = new ReviewDto();
        reviewDto.setId(reviewId);
        reviewDto.setMovieId(movieId);
        reviewDto.setReviewerName("testUser");
        reviewDto.setComment("Great movie!");
        reviewDto.setRating(5);
    }

    @Test
    void deleteReview_shouldRedirectToMovieDetails_whenSuccessful() {
        // Given
        when(reviewService.getReviewById(reviewId)).thenReturn(reviewDto);
        doNothing().when(reviewService).deleteReview(reviewId);

        // When
        String viewName = reviewWebController.deleteReview(reviewId);

        // Then
        assertEquals("redirect:/movies/details/" + movieId, viewName);
        verify(reviewService, times(1)).getReviewById(reviewId);
        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    @Test
    void deleteReview_shouldRedirectToMovies_whenExceptionThrown() {
        // Given
        when(reviewService.getReviewById(reviewId)).thenThrow(new RuntimeException("Error"));

        // When
        String viewName = reviewWebController.deleteReview(reviewId);

        // Then
        assertEquals("redirect:/movies/", viewName);
        verify(reviewService, times(1)).getReviewById(reviewId);
        verify(reviewService, never()).deleteReview(any());
    }

    @Test
    void editReviewForm_shouldReturnEditView_whenSuccessful() {
        // Given
        when(reviewService.getReviewById(reviewId)).thenReturn(reviewDto);

        // When
        String viewName = reviewWebController.editReviewForm(reviewId, model);

        // Then
        assertEquals("edit", viewName);
        verify(reviewService, times(1)).getReviewById(reviewId);
        verify(model, times(1)).addAttribute("review", reviewDto);
    }

    @Test
    void editReviewForm_shouldRedirectToMovies_whenExceptionThrown() {
        // Given
        when(reviewService.getReviewById(reviewId)).thenThrow(new RuntimeException("Error"));

        // When
        String viewName = reviewWebController.editReviewForm(reviewId, model);

        // Then
        assertEquals("redirect:/movies/", viewName);
        verify(reviewService, times(1)).getReviewById(reviewId);
        verify(model, never()).addAttribute(eq("review"), any());
    }

    @Test
    void updateReview_shouldRedirectToMovieDetails_whenSuccessful() {
        // Given
        when(reviewService.updateReview(eq(reviewId), any(ReviewDto.class))).thenReturn(reviewDto);

        // When
        String viewName = reviewWebController.updateReview(reviewId, reviewDto);

        // Then
        assertEquals("redirect:/movies/details/" + movieId, viewName);
        verify(reviewService, times(1)).updateReview(eq(reviewId), any(ReviewDto.class));
    }

    @Test
    void updateReview_shouldRedirectToMovies_whenExceptionThrown() {
        // Given
        when(reviewService.updateReview(eq(reviewId), any(ReviewDto.class)))
                .thenThrow(new RuntimeException("Error"));

        // When
        String viewName = reviewWebController.updateReview(reviewId, reviewDto);

        // Then
        assertEquals("redirect:/movies/", viewName);
        verify(reviewService, times(1)).updateReview(eq(reviewId), any(ReviewDto.class));
    }
}