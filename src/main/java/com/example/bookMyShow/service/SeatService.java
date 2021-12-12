package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.model.response.SeatResponse;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.util.Utils;

@Service
@AllArgsConstructor
public class SeatService
{
	private final SeatRepository seatRepository;
	private final Utils utils;

	@SneakyThrows
	public Seat getSeat(String seatId)
	{
		Seat seat = seatRepository.findBySeatId(seatId);
		if(ObjectUtils.isEmpty(seat))
		{
			String message = String.format("No seat found with seatId: %s", seatId);
			utils.throwServiceException(message);
		}
		return seat;
	}

	public SeatResponse getSeatResponse(Seat seat)
	{
		return SeatResponse.builder()
		                   .seatId(seat.getSeatId())
		                   .seatNo(seat.getSeatNo())
		                   .build();
	}
}
