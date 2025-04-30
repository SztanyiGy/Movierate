package com.example.movierate.controller;

import com.example.movierate.dto.MovieDto;
import com.example.movierate.service.Movieservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Movieservice movieService;

    @GetMapping("/")
    public String index(Model model) {
        List<MovieDto> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "index";
    }
}
