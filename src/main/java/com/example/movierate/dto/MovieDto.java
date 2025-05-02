package com.example.movierate.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String director;
    private int year;
    private Integer runtime;
    private String genre;
    private String img;
    private String trailer;
    private String actors;
    private String description;
    private List<ReviewDto> reviews;
}
