package com.example.movierate.controllers;

import com.example.movierate.controller.review.ReviewRestController;
import com.example.movierate.dto.ReviewDto;
import com.example.movierate.service.Reviewservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReviewRestControllerTest {

    @Mock
    private Reviewservice reviewService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private ReviewRestController reviewController;

    private ReviewDto reviewDto;
    private List<ReviewDto> reviewDtoList;
    private Long movieId;
    private Long reviewId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tesztadatok inicializálása
        movieId = 1L;
        reviewId = 2L;

        reviewDto = new ReviewDto();
        reviewDto.setId(reviewId);
        reviewDto.setReviewerName("testUser");
        reviewDto.setComment("Great movie!");
        reviewDto.setRating(5);

        ReviewDto reviewDto2 = new ReviewDto();
        reviewDto2.setId(3L);
        reviewDto2.setReviewerName("anotherUser");
        reviewDto2.setComment("Nice film!");
        reviewDto2.setRating(4);

        reviewDtoList = Arrays.asList(reviewDto, reviewDto2);

        // UserDetails mock beállítása
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void getReviewsByMovie_shouldReturnReviewsList() {
        // Given
        when(reviewService.getReviewsByMovieId(movieId)).thenReturn(reviewDtoList);

        // When
        ResponseEntity<List<ReviewDto>> response = reviewController.getReviewsByMovie(movieId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDtoList, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(reviewService, times(1)).getReviewsByMovieId(movieId);
    }

    @Test
    void getReviewsByMovie_shouldReturnEmptyList_whenNoReviewsFound() {
        // Given
        List<ReviewDto> emptyList = List.of();
        when(reviewService.getReviewsByMovieId(movieId)).thenReturn(emptyList);

        // When
        ResponseEntity<List<ReviewDto>> response = reviewController.getReviewsByMovie(movieId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(reviewService, times(1)).getReviewsByMovieId(movieId);
    }

    @Test
    void addReview_shouldReturnSavedReview() {
        // Given
        when(reviewService.addReviewToMovie(eq(movieId), any(ReviewDto.class))).thenReturn(reviewDto);

        // When
        ResponseEntity<ReviewDto> response = reviewController.addReview(movieId, reviewDto, userDetails);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDto, response.getBody());
        assertEquals("testUser", reviewDto.getReviewerName());
        verify(reviewService, times(1)).addReviewToMovie(eq(movieId), any(ReviewDto.class));
        verify(userDetails, times(1)).getUsername();
    }

    @Test
    void updateReview_shouldReturnUpdatedReview() {
        // Given
        ReviewDto updatedReview = new ReviewDto();
        updatedReview.setId(reviewId);
        updatedReview.setReviewerName("testUser");
        updatedReview.setComment("Updated content");
        updatedReview.setRating(4);

        when(reviewService.updateReview(eq(reviewId), any(ReviewDto.class))).thenReturn(updatedReview);

        // When
        ResponseEntity<ReviewDto> response = reviewController.updateReview(reviewId, reviewDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedReview, response.getBody());
        assertEquals("Updated content", response.getBody().getComment());
        verify(reviewService, times(1)).updateReview(eq(reviewId), any(ReviewDto.class));
    }

    @Test
    void deleteReview_shouldReturnRedirectString() {
        // Given
        doNothing().when(reviewService).deleteReview(reviewId);

        // When
        String response = reviewController.deleteReview(reviewId, movieId);

        // Then
        assertEquals("redirect:/movies/" + movieId, response);
        verify(reviewService, times(1)).deleteReview(reviewId);
    }
}