package com.example.movierate.repository;

import com.example.movierate.model.Moviemodel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Movierepository extends JpaRepository<Moviemodel, Long> {
}
