package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.example.bookMyShow.entity.PreBooking;
import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.entity.Show;
import com.example.bookMyShow.model.response.InitialBookingResponse;
import com.example.bookMyShow.repository.PreBookingRepository;
import com.example.bookMyShow.util.Utils;

@Slf4j
@Service
@AllArgsConstructor
public class PreBookingService
{
	private final PreBookingRepository preBookingRepository;
	private final SeatService seatService;
	private final ShowService showService;
	private final Utils utils;

	@Transactional
	public PreBooking getPreBooking(Seat seat, Show show)
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

	public InitialBookingResponse getInitialBookingResponse(PreBooking preBooking)
	{
		return InitialBookingResponse.builder()
		                             .preBookingId(preBooking.getPreBookingId())
		                             .seatResponse(seatService.getSeatResponse(preBooking.getSeat()))
		                             .showResponse(showService.mapToShowResponse(preBooking.getShow()))
			                         .build();
	}
}
