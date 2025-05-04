package com.example.movierate.service;

import com.example.movierate.dto.ReviewDto;
import com.example.movierate.exception.MovieException;
import com.example.movierate.model.Moviemodel;
import com.example.movierate.model.Reviewmodel;
import com.example.movierate.repository.MovieRepository;
import com.example.movierate.repository.ReviewRepository;
import com.example.movierate.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Moviemodel testMovie;
    private Reviewmodel testReview;
    private ReviewDto testReviewDto;
    private final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup SecurityContext mock
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TEST_USERNAME);
        SecurityContextHolder.setContext(securityContext);

        // Initialize test movie
        testMovie = new Moviemodel();
        testMovie.setId(1L);
        testMovie.setTitle("The Dark Knight");
        testMovie.setReviews(new ArrayList<>());

        // Initialize test review
        testReview = new Reviewmodel();
        testReview.setId(1L);
        testReview.setReviewerName(TEST_USERNAME);
        testReview.setScore(8);
        testReview.setComment("Great movie!");
        testReview.setMovie(testMovie);

        // Add review to movie
        testMovie.getReviews().add(testReview);

        // Initialize test review DTO
        testReviewDto = new ReviewDto();
        testReviewDto.setId(1L);
        testReviewDto.setReviewerName(TEST_USERNAME);
        testReviewDto.setRating(8);
        testReviewDto.setComment("Great movie!");
        testReviewDto.setMovieId(1L);
    }

    @Test
    void getReviewsByMovieId_ShouldReturnReviewDtos() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));

        // Act
        List<ReviewDto> result = reviewService.getReviewsByMovieId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_USERNAME, result.get(0).getReviewerName());
        assertEquals(8, result.get(0).getRating());
        assertEquals("Great movie!", result.get(0).getComment());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getReviewsByMovieId_ShouldThrowExceptionWhenMovieNotFound() {
        // Arrange
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MovieException.class, () -> {
            reviewService.getReviewsByMovieId(999L);
        });
        verify(movieRepository, times(1)).findById(999L);
    }

    @Test
    void addReviewToMovie_ShouldSaveReviewWithLoggedInUsername() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(reviewRepository.save(any(Reviewmodel.class))).thenReturn(testReview);

        ReviewDto inputDto = new ReviewDto();
        inputDto.setRating(8);
        inputDto.setComment("Great movie!");

        // Act
        ReviewDto result = reviewService.addReviewToMovie(1L, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getReviewerName()); // Should use logged in username
        assertEquals(8, result.getRating());
        assertEquals("Great movie!", result.getComment());
        verify(movieRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Reviewmodel.class));
    }

    @Test
    void addReviewToMovie_ShouldThrowExceptionWhenMovieNotFound() {
        // Arrange
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MovieException.class, () -> {
            reviewService.addReviewToMovie(999L, testReviewDto);
        });
        verify(movieRepository, times(1)).findById(999L);
        verify(reviewRepository, never()).save(any(Reviewmodel.class));
    }

    @Test
    void updateReview_ShouldUpdateExistingReview() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // Updated review data
        Reviewmodel updatedReview = new Reviewmodel();
        updatedReview.setId(1L);
        updatedReview.setReviewerName("updatedUser");
        updatedReview.setScore(9);
        updatedReview.setComment("Even better on second viewing!");
        updatedReview.setMovie(testMovie);

        when(reviewRepository.save(any(Reviewmodel.class))).thenReturn(updatedReview);

        ReviewDto inputDto = new ReviewDto();
        inputDto.setReviewerName("updatedUser");
        inputDto.setRating(9);
        inputDto.setComment("Even better on second viewing!");

        // Act
        ReviewDto result = reviewService.updateReview(1L, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("updatedUser", result.getReviewerName());
        assertEquals(9, result.getRating());
        assertEquals("Even better on second viewing!", result.getComment());
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Reviewmodel.class));
    }

    @Test
    void updateReview_ShouldThrowExceptionWhenReviewNotFound() {
        // Arrange
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MovieException.class, () -> {
            reviewService.updateReview(999L, testReviewDto);
        });
        verify(reviewRepository, times(1)).findById(999L);
        verify(reviewRepository, never()).save(any(Reviewmodel.class));
    }

    @Test
    void deleteReview_ShouldCallRepositoryDelete() {
        // Act
        reviewService.deleteReview(1L);

        // Assert
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void calculateAverageRatingForMovie_ShouldReturnCorrectAverage() {
        // Arrange
        // Add more reviews to the movie
        Reviewmodel review2 = new Reviewmodel();
        review2.setId(2L);
        review2.setReviewerName("user2");
        review2.setScore(6);
        review2.setMovie(testMovie);

        Reviewmodel review3 = new Reviewmodel();
        review3.setId(3L);
        review3.setReviewerName("user3");
        review3.setScore(10);
        review3.setMovie(testMovie);

        List<Reviewmodel> reviews = new ArrayList<>();
        reviews.add(testReview);  // Score: 8
        reviews.add(review2);     // Score: 6
        reviews.add(review3);     // Score: 10
        testMovie.setReviews(reviews);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));

        // Act
        double result = reviewService.calculateAverageRatingForMovie(1L);

        // Assert
        assertEquals(8.0, result); // (8 + 6 + 10) / 3 = 8.0
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void calculateAverageRatingForMovie_ShouldReturnZeroForNoReviews() {
        // Arrange
        testMovie.setReviews(new ArrayList<>());
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));

        // Act
        double result = reviewService.calculateAverageRatingForMovie(1L);

        // Assert
        assertEquals(0.0, result);
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getReviewById_ShouldReturnReviewDto() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // Act
        ReviewDto result = reviewService.getReviewById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(TEST_USERNAME, result.getReviewerName());
        assertEquals(8, result.getRating());
        assertEquals("Great movie!", result.getComment());
        assertEquals(1L, result.getMovieId());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void getReviewById_ShouldThrowExceptionWhenReviewNotFound() {
        // Arrange
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MovieException.class, () -> {
            reviewService.getReviewById(999L);
        });
        verify(reviewRepository, times(1)).findById(999L);
    }
}