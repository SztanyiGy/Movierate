package com.example.movierate.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moviemodel")
@Getter
@Setter
public class Moviemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String director;
    @Column(name = "release_year")
    private int year;
    private String genre;
    @Column(name = "img")
    private String img;
    private String trailer;
    private String actors;
    @Column(columnDefinition = "TEXT")
    private String description;


    @OneToMany(mappedBy = "movie",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviewmodel> reviews = new ArrayList<>();
}
