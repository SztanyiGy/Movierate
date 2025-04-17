package com.example.movierate.repository;

import com.example.movierate.model.Usermodel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Usermodel, Long> {
    Usermodel findByUsername(String username);
}
