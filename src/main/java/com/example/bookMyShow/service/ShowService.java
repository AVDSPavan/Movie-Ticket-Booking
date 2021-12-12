package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Movie;
import com.example.bookMyShow.entity.Show;
import com.example.bookMyShow.model.response.ShowResponse;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.util.Utils;

@Service
@AllArgsConstructor
public class ShowService
{
	private final ShowRepository showRepository;
	private final Utils utils;

	@SneakyThrows
	public Show getShowByShowId(String showId)
	{
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("No show found with id: %s", showId);
			utils.throwServiceException(message);
		}
		return show;
	}

	public ShowResponse mapToShowResponse(Show show)
	{
		return ShowResponse.builder().showId(show.getShowId())
		                   .startTime(utils.getStrDate(show.getStartTime()))
		                   .endTime(utils.getStrDate(show.getEndTime()))
		                   .movieId(show.getMovie().getMovieId())
		                   .movieTitle(show.getMovie().getTitle())
		                   .cinemaHallId(show.getCinemaHall().getCinemaHallId())
		                   .cinemaHallName(show.getCinemaHall().getName())
		                   .createdAt(utils.getStrDate(show.getCreatedAt()))
		                   .updatedAt(utils.getStrDate(show.getUpdatedAt()))
		                   .build();
	}

	public Show getShowEntity(Movie movie, CinemaHall cinemaHall, Date startTime, Date endTime, Date date)
	{
		Show show = new Show();
		show.setCinemaHall(cinemaHall);
		show.setMovie(movie);
		show.setStartTime(startTime);
		show.setEndTime(endTime);
		show.setDate(date);
		show.setCreatedAt(utils.getCurrentDate());
		show.setUpdatedAt(utils.getCurrentDate());
		return show;
	}

}
