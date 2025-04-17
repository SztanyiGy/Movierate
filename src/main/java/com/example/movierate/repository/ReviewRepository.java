package com.example.movierate.repository;

import com.example.movierate.model.Reviewmodel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Reviewmodel, Long> {
}
