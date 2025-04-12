package com.example.movierate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REVIEWMODEL")
@Getter  // Lombok getterek automatikus generálása
@Setter  // Lombok setterek automatikus generálása
public class Reviewmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewerName;
    private int score; // vagy rating, ha ezt használod

    private String comment;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Moviemodel movie;
}
