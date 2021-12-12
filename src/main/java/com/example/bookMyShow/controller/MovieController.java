package com.example.bookMyShow.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookMyShow.model.request.AddMovieRequest;
import com.example.bookMyShow.model.request.UpdateMovieRequest;
import com.example.bookMyShow.model.response.MovieResponse;

@RequestMapping("/movie")
public interface MovieController
{
	@GetMapping(path = "/getAll")
	List<MovieResponse> getAllMovies();

	@GetMapping(path = "/get")
	MovieResponse getMovie(@RequestParam("id") String movieId);

	@PostMapping(path = "/add")
	MovieResponse addMovie(@Validated @RequestBody AddMovieRequest addMovieRequest);

	@PutMapping(path = "/update")
	MovieResponse updateMovie(@Validated @RequestBody UpdateMovieRequest updateMovieRequest);

	@DeleteMapping(path = "/delete")
	void deleteMovie(@RequestParam("id") String movieId);
}
