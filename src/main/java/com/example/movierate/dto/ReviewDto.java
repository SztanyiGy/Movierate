package com.example.movierate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private String reviewerName;
    private int rating;
    private String comment;
    private Long movieId;

}
