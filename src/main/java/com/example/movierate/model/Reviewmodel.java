package com.example.movierate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
    private int score;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Moviemodel movie;
}
