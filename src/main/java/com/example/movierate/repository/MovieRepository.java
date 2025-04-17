package com.example.movierate.repository;

import com.example.movierate.model.Moviemodel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Moviemodel, Long> {
}
