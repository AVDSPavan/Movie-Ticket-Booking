package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookMyShow.entity.Booking;
import com.example.bookMyShow.entity.BookingStatus;
import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Movie;
import com.example.bookMyShow.entity.PreBooking;
import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.entity.Show;
import com.example.bookMyShow.exception.ServiceException;
import com.example.bookMyShow.model.request.AddShowRequest;
import com.example.bookMyShow.model.response.ShowResponse;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.CinemaHallRepository;
import com.example.bookMyShow.repository.MovieRepository;
import com.example.bookMyShow.repository.PreBookingRepository;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class ShowControllerImpl implements ShowController
{
	private final ShowRepository showRepository;
	private final MovieRepository movieRepository;
	private final CinemaHallRepository cinemaHallRepository;
	private final BookingRepository bookingRepository;
	private final PreBookingRepository preBookingRepository;
	private final SeatRepository seatRepository;
	private final Utils utils;

	@Override
	public List<ShowResponse> getAllShows()
	{
		List<Show> showList = showRepository.findAll();
		List<ShowResponse> showResponseList = new ArrayList<>();
		for(Show show : showList)
		{
			showResponseList.add(mapToShowResponse(show));
		}
		return showResponseList;
	}

	@Override
	public List<ShowResponse> getShowsByMovieTitle(String movieTitle)
	{
		List<Show> showList = showRepository.findByMovieTitle(movieTitle);
		List<ShowResponse> showResponseList = new ArrayList<>();
		for(Show show : showList)
		{
			showResponseList.add(mapToShowResponse(show));
		}
		return showResponseList;
	}

	@Override
	@SneakyThrows
	public List<Seat> getAvailableSeats(String showId)
	{
		Show show = getShowByShowId(showId);
		List<Seat> seatList = seatRepository.findByCinemaHall(show.getCinemaHall());
		List<Booking> bookingList = bookingRepository.findByShowAndBookingStatus(show, BookingStatus.CONFIRMED);
		List<PreBooking> preBookingList = preBookingRepository.findByShow(show);
		List<Seat> bookedSeats = new ArrayList<>();
		for(Booking booking: bookingList) {
			bookedSeats.add(booking.getSeat());
		}
		for(PreBooking preBooking: preBookingList) {
			bookedSeats.add(preBooking.getSeat());
		}
		seatList.removeAll(bookedSeats);
		return seatList;
	}

	@SneakyThrows
	private Show getShowByShowId(String showId)
	{
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("No show found with id: %s", showId);
			utils.throwServiceException(message);
		}
		return show;
	}

	@Override
	@SneakyThrows
	public List<Seat> getBookedSeats(String showId)
	{
		Show show = getShowByShowId(showId);
		List<Booking> bookingList = bookingRepository.findByShowAndBookingStatus(show, BookingStatus.CONFIRMED);
		List<Seat> bookedSeats = new ArrayList<>();
		for(Booking booking: bookingList) {
			bookedSeats.add(booking.getSeat());
		}
		return bookedSeats;
	}

	@Override
	@SneakyThrows
	public ShowResponse getShow(String showId)
	{
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("No show found with id: %s", showId);
			utils.throwServiceException(message);
		}
		return mapToShowResponse(show);
	}

	@Override
	@SneakyThrows
	public ShowResponse addShow(AddShowRequest addShowRequest)
	{
		String movieId = addShowRequest.getMovieId();
		String cinemaHallId = addShowRequest.getCinemaHallId();
		Movie movie = getMovieEntity(movieId);
		CinemaHall cinemaHall = getCinemaHallEntity(cinemaHallId);

		Date startTime = utils.getDateFromStr(addShowRequest.getStartTime());
		Date endTime = DateUtils.addMinutes(startTime, movie.getDuration()+15);
		Date date = utils.getFormattedDate(startTime);
		List<Show> shows = showRepository.findByDateAndMovieAndCinemaHallOrderByStartTime(date, movie, cinemaHall);
		boolean slotExists = shows.isEmpty();
		for(Show existingShow : shows)
		{
			Date existingShowStartTime = existingShow.getStartTime(), existingShowEndTime = existingShow.getStartTime();
			if(existingShowStartTime.before(startTime) && existingShowEndTime.before(startTime) ||
				existingShowStartTime.after(startTime) && existingShowStartTime.after(endTime))
			{
				slotExists = true;
				break;
			}
		}
		if(slotExists) {
			Show show = getShowEntity(movie, cinemaHall, startTime, endTime, date);
			return mapToShowResponse(showRepository.save(show));
		}
		utils.throwServiceException("No slot found with given startTime and duration");
		return null;
	}

	private ShowResponse mapToShowResponse(Show show)
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

	private CinemaHall getCinemaHallEntity(String cinemaHallId) throws ServiceException
	{
		CinemaHall cinemaHall = cinemaHallRepository.findByCinemaHallId(cinemaHallId);
		if(ObjectUtils.isEmpty(cinemaHall))
		{
			String message = String.format("cinemaHall not found with id: %s", cinemaHallId);
			utils.throwServiceException(message);
		}
		return cinemaHall;
	}

	private Movie getMovieEntity(String movieId) throws ServiceException
	{
		Movie movie = movieRepository.findByMovieId(movieId);
		if(ObjectUtils.isEmpty(movie))
		{
			String message = String.format("movie not found with id: %s", movieId);
			utils.throwServiceException(message);
		}
		return movie;
	}

	private Show getShowEntity(Movie movie, CinemaHall cinemaHall, Date startTime, Date endTime, Date date)
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

	@Override
	@SneakyThrows
	public Show updateShow(Show showRequest)
	{
		String showId = showRequest.getShowId();
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("movie not found with id: %s", showId);
			utils.throwServiceException(message);
		}
		return showRepository.save(showRequest);
	}

	@Override
	@SneakyThrows
	public void deleteShow(String showId)
	{
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("movie not found with id: %s", showId);
			utils.throwServiceException(message);
		}
		showRepository.delete(show);
	}
}
