package com.example.movierate.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String director;
    private int year;
    private String genre;
    private List<ReviewDto> reviews;
}
