package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.bookMyShow.entity.Movie;
import com.example.bookMyShow.exception.ServiceException;
import com.example.bookMyShow.model.request.AddMovieRequest;
import com.example.bookMyShow.model.request.UpdateMovieRequest;
import com.example.bookMyShow.model.response.MovieResponse;
import com.example.bookMyShow.repository.MovieRepository;
import com.example.bookMyShow.util.Utils;

@Service
@AllArgsConstructor
public class MovieService
{
	private final MovieRepository movieRepository;
	private final Utils utils;

	public Movie getMovieEntity(String movieId) throws ServiceException
	{
		Movie movie = movieRepository.findByMovieId(movieId);
		if(ObjectUtils.isEmpty(movie))
		{
			String message = String.format("movie not found with id: %s", movieId);
			utils.throwServiceException(message);
		}
		return movie;
	}

	public MovieResponse mapToMovieResponse(Movie movie)
	{
		return MovieResponse.builder().movieId(movie.getMovieId())
		                    .title(movie.getTitle()).description(movie.getDescription())
		                    .durationInMinutes(movie.getDuration()).language(movie.getLanguage())
		                    .createdAt(utils.getStrDate(movie.getCreatedAt()))
		                    .updatedAt(utils.getStrDate(movie.getUpdatedAt()))
		                    .build();
	}

	public Movie mapToMovieEntityFromAddMovieRequest(AddMovieRequest addMovieRequest)
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

	public Movie mapToMovieEntityFromUpdateMovieRequest(Movie movie, UpdateMovieRequest updateMovieRequest)
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
