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
import com.example.bookMyShow.model.request.AddShowRequest;
import com.example.bookMyShow.model.response.SeatResponse;
import com.example.bookMyShow.model.response.ShowResponse;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.PreBookingRepository;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.service.CinemaHallService;
import com.example.bookMyShow.service.MovieService;
import com.example.bookMyShow.service.SeatService;
import com.example.bookMyShow.service.ShowService;
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class ShowControllerImpl implements ShowController
{
	private final PreBookingRepository preBookingRepository;
	private final BookingRepository bookingRepository;
	private final CinemaHallService cinemaHallService;
	private final SeatRepository seatRepository;
	private final ShowRepository showRepository;
	private final ShowService showService;
	private final SeatService seatService;
	private final MovieService movieService;
	private final Utils utils;

	@Override
	public List<ShowResponse> getAllShows()
	{
		List<Show> showList = showRepository.findAll();
		List<ShowResponse> showResponseList = new ArrayList<>();
		for(Show show : showList)
		{
			showResponseList.add(showService.mapToShowResponse(show));
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
			showResponseList.add(showService.mapToShowResponse(show));
		}
		return showResponseList;
	}

	@Override
	@SneakyThrows
	public List<SeatResponse> getAvailableSeats(String showId)
	{
		Show show = showService.getShowByShowId(showId);
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
		List<SeatResponse> seatResponseList = new ArrayList<>();
		for(Seat seat: seatList) {
			seatResponseList.add(seatService.getSeatResponse(seat));
		}
		return seatResponseList;
	}

	@Override
	@SneakyThrows
	public List<SeatResponse> getBookedSeats(String showId)
	{
		Show show = showService.getShowByShowId(showId);
		List<Booking> bookingList = bookingRepository.findByShowAndBookingStatus(show, BookingStatus.CONFIRMED);
		List<Seat> bookedSeats = new ArrayList<>();
		for(Booking booking: bookingList) {
			bookedSeats.add(booking.getSeat());
		}
		List<SeatResponse> seatResponseList = new ArrayList<>();
		for(Seat seat : bookedSeats) {
			seatResponseList.add(seatService.getSeatResponse(seat));
		}
		return seatResponseList;
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
		return showService.mapToShowResponse(show);
	}

	@Override
	@SneakyThrows
	public ShowResponse addShow(AddShowRequest addShowRequest)
	{
		String movieId = addShowRequest.getMovieId();
		String cinemaHallId = addShowRequest.getCinemaHallId();
		Movie movie = movieService.getMovieEntity(movieId);
		CinemaHall cinemaHall = cinemaHallService.getCinemaHallEntity(cinemaHallId);

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
			Show show = showService.getShowEntity(movie, cinemaHall, startTime, endTime, date);
			return showService.mapToShowResponse(showRepository.save(show));
		}
		utils.throwServiceException("No slot found with given startTime and duration");
		return null;
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
