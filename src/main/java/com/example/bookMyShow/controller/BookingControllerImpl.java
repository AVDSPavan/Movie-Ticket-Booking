package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.print.Book;
import java.util.ArrayList;
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
import com.example.bookMyShow.model.response.BookingResponse;
import com.example.bookMyShow.model.response.InitialBookingResponse;
import com.example.bookMyShow.model.response.ShowResponse;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.repository.PreBookingRepository;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.repository.ShowRepository;
import com.example.bookMyShow.service.BookingService;
import com.example.bookMyShow.service.PreBookingService;
import com.example.bookMyShow.service.SeatService;
import com.example.bookMyShow.service.ShowService;
import com.example.bookMyShow.util.Utils;

@Slf4j
@RestController
@AllArgsConstructor
public class BookingControllerImpl implements BookingController
{
	private final PreBookingRepository preBookingRepository;
	private final BookingRepository bookingRepository;
	private final PreBookingService preBookingService;
	private final BookingService bookingService;
	private final ShowService showService;
	private final SeatService seatService;
	private final Utils utils;

	@Override
	public List<BookingResponse> getAllBookings()
	{
		List<Booking> bookingList = bookingRepository.findAll();
		List<BookingResponse> bookingResponseList = new ArrayList<>();
		for(Booking booking : bookingList)
		{
			bookingResponseList.add(bookingService.getBookingResponse(booking));
		}
		return bookingResponseList;
	}

	@Override
	@SneakyThrows
	public BookingResponse getBooking(String bookingId)
	{
		Booking booking = bookingRepository.findByBookingId(bookingId);
		if(ObjectUtils.isEmpty(booking))
		{
			String message = String.format("No booking found with id: %s", bookingId);
			utils.throwServiceException(message);
		}
		return bookingService.getBookingResponse(booking);
	}

	@Override
	@SneakyThrows
	public InitialBookingResponse addBooking(AddBookingRequest addBookingRequest)
	{
		String seatId = addBookingRequest.getSeatId();
		String showId = addBookingRequest.getShowId();
		Seat seat = seatService.getSeat(seatId);
		Show show = showService.getShowByShowId(showId);
		PreBooking preBooking = preBookingRepository.findBySeatIdAndShowId(seatId, showId);
		Booking booking = bookingRepository.findBySeatIdAndShowId(seatId, showId);
		if(!ObjectUtils.isEmpty(preBooking) || !ObjectUtils.isEmpty(booking))
		{
			String message = String.format("booking already exists with seatId: %s and showId: %s", seatId, showId);
			utils.throwServiceException(message);
		}
		preBooking = preBookingService.getPreBooking(seat, show);
		return preBookingService.getInitialBookingResponse(preBooking);
	}

	@Override
	@SneakyThrows
	public BookingResponse completeBooking(String preBookingId)
	{
		PreBooking preBooking = preBookingRepository.findByPreBookingId(preBookingId);
		if(ObjectUtils.isEmpty(preBooking))
		{
			String message = String.format("preBookingId: %s not found or expired", preBooking);
			utils.throwServiceException(message);
		}
		Booking booking = bookingService.getBookingCompletedDetails(preBooking);
		return bookingService.getBookingResponse(booking);
	}

	@Override
	@SneakyThrows
	public BookingResponse updateBooking(Booking bookingRequest)
	{
		String bookingId = bookingRequest.getBookingId();
		Booking booking = bookingRepository.findByBookingId(bookingId);
		if(ObjectUtils.isEmpty(booking))
		{
			String message = String.format("booking not found with id: %s", bookingId);
			utils.throwServiceException(message);
		}
		booking = bookingRepository.save(bookingRequest);
		return bookingService.getBookingResponse(booking);
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
