package com.example.movierate.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String reviewerName;
    private int rating;  // Ha rating helyett score-t használsz, változtasd meg itt is.
    private String comment;
    private Long movieId;

    // Kiegészítő konstruktor
    public ReviewDto(Long id, String reviewerName, int rating, String comment, Long movieId) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.rating = rating;  // Ha rating helyett score-t használsz, változtasd meg itt is.
        this.comment = comment;
        this.movieId = movieId;
    }

    public ReviewDto() {

    }
}
