package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.bookMyShow.entity.Booking;
import com.example.bookMyShow.entity.BookingStatus;
import com.example.bookMyShow.entity.PreBooking;
import com.example.bookMyShow.model.response.BookingResponse;
import com.example.bookMyShow.repository.BookingRepository;
import com.example.bookMyShow.util.Utils;

@Slf4j
@Service
@AllArgsConstructor
public class BookingService
{
	private final BookingRepository bookingRepository;
	private final SeatService seatService;
	private final ShowService showService;
	private final Utils utils;

	@Transactional
	public Booking getBookingCompletedDetails(PreBooking preBooking)
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

	public BookingResponse getBookingResponse(Booking booking)
	{
		return BookingResponse.builder()
		                      .bookingId(booking.getBookingId())
		                      .bookingStatus(booking.getBookingStatus())
		                      .seatResponse(seatService.getSeatResponse(booking.getSeat()))
		                      .showResponse(showService.mapToShowResponse(booking.getShow()))
		                      .build();
	}
}
