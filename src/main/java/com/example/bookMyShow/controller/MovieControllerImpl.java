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
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class MovieControllerImpl implements MovieController
{
	private final MovieRepository movieRepository;
	private final Utils utils;

	@Override
	public List<MovieResponse> getAllMovies()
	{
		List<Movie> movieList = movieRepository.findAll();
		List<MovieResponse> movieResponseList = new ArrayList<>();
		for(Movie movie : movieList)
		{
			movieResponseList.add(mapToMovieResponse(movie));
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
		return mapToMovieResponse(movie);
	}

	@Override
	@SneakyThrows
	public MovieResponse addMovie(AddMovieRequest addMovieRequest)
	{
		Movie movie = movieRepository.save(mapToMovieEntityFromAddMovieRequest(addMovieRequest));
		return mapToMovieResponse(movie);
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
		movie = movieRepository.save(mapToMovieEntityFromUpdateMovieRequest(movie, updateMovieRequest));
		return mapToMovieResponse(movie);
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

	private MovieResponse mapToMovieResponse(Movie movie)
	{
		return MovieResponse.builder().movieId(movie.getMovieId())
		                    .title(movie.getTitle()).description(movie.getDescription())
		                    .durationInMinutes(movie.getDuration()).language(movie.getLanguage())
		                    .createdAt(utils.getStrDate(movie.getCreatedAt()))
		                    .updatedAt(utils.getStrDate(movie.getUpdatedAt()))
		                    .build();
	}

	private Movie mapToMovieEntityFromAddMovieRequest(AddMovieRequest addMovieRequest)
	{
		Movie movie = new Movie();
		movie.setTitle(addMovieRequest.getTitle());
		movie.setDescription(addMovieRequest.getDescription());
		movie.setDuration(addMovieRequest.getDurationInMinutes());
		movie.setLanguage(addMovieRequest.getLanguage());
		movie.setCreatedAt(utils.getCurrentDate());
		movie.setUpdatedAt(utils.getCurrentDate());
		return movie;
	}

	private Movie mapToMovieEntityFromUpdateMovieRequest(Movie movie, UpdateMovieRequest updateMovieRequest)
	{
		if(!ObjectUtils.isEmpty(updateMovieRequest.getTitle())){
			movie.setTitle(updateMovieRequest.getTitle());
		}
		if(!ObjectUtils.isEmpty(updateMovieRequest.getDescription())){
			movie.setDescription(updateMovieRequest.getDescription());
		}
		if(!ObjectUtils.isEmpty(updateMovieRequest.getDurationInMinutes())) {
			movie.setDuration(updateMovieRequest.getDurationInMinutes());
		}
		if(!ObjectUtils.isEmpty(updateMovieRequest.getLanguage())) {
			movie.setLanguage(updateMovieRequest.getLanguage());
		}
		movie.setUpdatedAt(utils.getCurrentDate());
		return movie;
	}
}
