package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookMyShow.entity.Movie;
import com.example.bookMyShow.model.request.AddMovieRequest;
import com.example.bookMyShow.model.request.UpdateMovieRequest;
import com.example.bookMyShow.model.response.MovieResponse;
import com.example.bookMyShow.repository.MovieRepository;
import com.example.bookMyShow.service.MovieService;
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class MovieControllerImpl implements MovieController
{
	private final MovieRepository movieRepository;
	private final MovieService movieService;
	private final Utils utils;

	@Override
	public List<MovieResponse> getAllMovies()
	{
		List<Movie> movieList = movieRepository.findAll();
		List<MovieResponse> movieResponseList = new ArrayList<>();
		for(Movie movie : movieList)
		{
			movieResponseList.add(movieService.mapToMovieResponse(movie));
		}
		return movieResponseList;
	}

	@Override
	@SneakyThrows
	public MovieResponse getMovie(String movieId)
	{
		Movie movie = movieRepository.findByMovieId(movieId);
		if(ObjectUtils.isEmpty(movie))
		{
			String message = String.format("No movie found with id: %s", movieId);
			utils.throwServiceException(message);
		}
		return movieService.mapToMovieResponse(movie);
	}

	@Override
	@SneakyThrows
	public MovieResponse addMovie(AddMovieRequest addMovieRequest)
	{
		Movie movie = movieRepository.save(movieService.mapToMovieEntityFromAddMovieRequest(addMovieRequest));
		return movieService.mapToMovieResponse(movie);
	}

	@Override
	@SneakyThrows
	public MovieResponse updateMovie(UpdateMovieRequest updateMovieRequest)
	{
		String movieId = updateMovieRequest.getMovieId();
		Movie movie = movieRepository.findByMovieId(movieId);
		if(ObjectUtils.isEmpty(movie))
		{
			String message = String.format("movie not found with id: %s", movieId);
			utils.throwServiceException(message);
		}
		movie = movieRepository.save(movieService.mapToMovieEntityFromUpdateMovieRequest(movie, updateMovieRequest));
		return movieService.mapToMovieResponse(movie);
	}

	@Override
	@SneakyThrows
	public void deleteMovie(String movieId)
	{
		Movie movie = movieRepository.findByMovieId(movieId);
		if(ObjectUtils.isEmpty(movie))
		{
			String message = String.format("movie not found with id: %s", movieId);
			utils.throwServiceException(message);
		}
		movieRepository.delete(movie);
	}
}
