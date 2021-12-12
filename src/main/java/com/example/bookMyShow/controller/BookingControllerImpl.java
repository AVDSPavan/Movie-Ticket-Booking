package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookMyShow.entity.Booking;
import com.example.bookMyShow.entity.BookingStatus;
import com.example.bookMyShow.entity.PreBooking;
import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.entity.Show;
import com.example.bookMyShow.exception.ServiceException;
import com.example.bookMyShow.model.request.AddBookingRequest;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.PreBookingRepository;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class BookingControllerImpl implements BookingController
{
	private final BookingRepository bookingRepository;
	private final ShowRepository showRepository;
	private final SeatRepository seatRepository;
	private final PreBookingRepository preBookingRepository;
	private final Utils utils;

	@Override
	public List<Booking> getAllBookings()
	{
		return bookingRepository.findAll();
	}

	@Override
	@SneakyThrows
	public Booking getBooking(String bookingId)
	{
		Booking booking = bookingRepository.findByBookingId(bookingId);
		if(ObjectUtils.isEmpty(booking))
		{
			String message = String.format("No booking found with id: %s", bookingId);
			utils.throwServiceException(message);
		}
		return booking;
	}

	@Override
	@SneakyThrows
	public PreBooking addBooking(AddBookingRequest addBookingRequest)
	{
		String seatId = addBookingRequest.getSeatId();
		String showId = addBookingRequest.getShowId();
		Seat seat = getSeat(seatId);
		Show show = getShow(showId);
		PreBooking preBooking = preBookingRepository.findBySeatIdAndShowId(seatId, showId);
		Booking booking = bookingRepository.findBySeatIdAndShowId(seatId, showId);
		if(!ObjectUtils.isEmpty(preBooking) || !ObjectUtils.isEmpty(booking))
		{
			String message = String.format("booking already exists with seatId: %s and showId: %s", seatId, showId);
			utils.throwServiceException(message);
		}
		return getPreBooking(seat, show);
	}

	private Show getShow(String showId) throws ServiceException
	{
		Show show = showRepository.findByShowId(showId);
		if(ObjectUtils.isEmpty(show))
		{
			String message = String.format("No show found with showId: %s", showId);
			utils.throwServiceException(message);
		}
		return show;
	}

	private Seat getSeat(String seatId) throws ServiceException
	{
		Seat seat = seatRepository.findBySeatId(seatId);
		if(ObjectUtils.isEmpty(seat))
		{
			String message = String.format("No seat found with seatId: %s", seatId);
			utils.throwServiceException(message);
		}
		return seat;
	}

	@Override
	@SneakyThrows
	public Booking completeBooking(String preBookingId)
	{
		PreBooking preBooking = preBookingRepository.findByPreBookingId(preBookingId);
		if(ObjectUtils.isEmpty(preBooking))
		{
			String message = String.format("preBookingId: %s not found or expired", preBooking);
			utils.throwServiceException(message);
		}
		return getBookingCompletedDetails(preBooking);
	}

	@Transactional
	private Booking getBookingCompletedDetails(PreBooking preBooking)
	{
		Booking booking = new Booking();
		booking.setSeat(preBooking.getSeat());
		booking.setShow(preBooking.getShow());
		booking.setBookingStatus(BookingStatus.CONFIRMED);
		booking.setCreatedAt(utils.getCurrentDate());
		booking.setUpdatedAt(utils.getCurrentDate());
		booking =  bookingRepository.save(booking);
		log.info("Booking confirmed for seatId: {}, showId: {} with bookingId: {}", booking.getSeat().getSeatId(),
			booking.getShow().getShowId(), booking.getBookingId());
		return booking;
	}

	@Transactional
	private PreBooking getPreBooking(Seat seat, Show show)
	{
		PreBooking preBooking = new PreBooking();
		preBooking.setSeat(seat);
		preBooking.setShow(show);
		preBooking.setCreatedAt(utils.getCurrentDate());
		preBooking.setExpiryAt(DateUtils.addMinutes(utils.getCurrentDate(),5));
		preBooking = preBookingRepository.save(preBooking);
		log.info("seatId: {}, showId: {} are preBooked with preBookingId: {}", preBooking.getSeat().getSeatId(),
			preBooking.getShow().getShowId(), preBooking.getPreBookingId());
		return preBooking;
	}

	@Override
	@SneakyThrows
	public Booking updateBooking(Booking bookingRequest)
	{
		String bookingId = bookingRequest.getBookingId();
		Booking booking = bookingRepository.findByBookingId(bookingId);
		if(ObjectUtils.isEmpty(booking))
		{
			String message = String.format("booking not found with id: %s", bookingId);
			utils.throwServiceException(message);
		}
		return bookingRepository.save(bookingRequest);
	}

	@Override
	@SneakyThrows
	public void deleteBooking(String bookingId)
	{
		Booking booking = bookingRepository.findByBookingId(bookingId);
		if(ObjectUtils.isEmpty(booking))
		{
			String message = String.format("booking not found with id: %s", bookingId);
			utils.throwServiceException(message);
		}
		bookingRepository.delete(booking);
	}
}
