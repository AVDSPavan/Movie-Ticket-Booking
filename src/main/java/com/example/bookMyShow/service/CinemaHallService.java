package com.example.bookMyShow.service;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.exception.ServiceException;
import com.example.bookMyShow.model.request.AddCinemaHallRequest;
import com.example.bookMyShow.model.request.UpdateCinemaHallRequest;
import com.example.bookMyShow.model.response.CinemaHallResponse;
import com.example.bookMyShow.repository.CinemaHallRepository;
import com.example.bookMyShow.util.Utils;

@Service
@AllArgsConstructor
public class CinemaHallService
{
	private final CinemaHallRepository cinemaHallRepository;
	private final Utils utils;

	public CinemaHall getCinemaHallEntity(String cinemaHallId) throws ServiceException
	{
		CinemaHall cinemaHall = cinemaHallRepository.findByCinemaHallId(cinemaHallId);
		if(ObjectUtils.isEmpty(cinemaHall))
		{
			String message = String.format("cinemaHall not found with id: %s", cinemaHallId);
			utils.throwServiceException(message);
		}
		return cinemaHall;
	}

	public List<Seat> getSeatList(CinemaHall cinemaHall, List<List<Integer>> seatingLayout)
	{
		List<Seat> seatList = new ArrayList<>();
		int seatRowNo =1, seatColNo = 1;
		for(int gridRowNo = 0; gridRowNo< seatingLayout.size(); gridRowNo++)
		{
			for(int gridColumnNo = 0; gridColumnNo< seatingLayout.get(0).size(); gridColumnNo++)
			{
				if(seatingLayout.get(gridRowNo).get(gridColumnNo) != 0)
				{
					Seat seat = mapToSeatEntity(cinemaHall, seatRowNo, seatColNo, gridRowNo, gridColumnNo);
					seatList.add(seat);
					seatColNo++;
				}
			}
			seatRowNo++;
		}
		return seatList;
	}

	public int getSeatsCount(List<List<Integer>> seatingLayout)
	{
		int counter = 0;
		for(List<Integer> rowList : seatingLayout)
		{
			counter += rowList.stream().filter(number -> number != 0).count();
		}
		return counter;
	}

	public Seat mapToSeatEntity(CinemaHall cinemaHall, int seatRowNo, int seatColNo, int gridRowNo, int gridColumnNo)
	{
		String seatNo = String.format("R%sC%s", seatRowNo, seatColNo);
		Seat seat = new Seat();
		seat.setCinemaHall(cinemaHall);
		seat.setSeatNo(seatNo);
		seat.setRowNo(gridRowNo);
		seat.setColumnNo(gridColumnNo);
		return seat;
	}

	public CinemaHallResponse mapToCinemaHallResponse(CinemaHall cinemaHall)
	{
		return CinemaHallResponse.builder().cinemaHallId(cinemaHall.getCinemaHallId())
		                         .name(cinemaHall.getName()).totalSeats(cinemaHall.getTotalSeats())
		                         .seatingLayout(cinemaHall.getSeatingLayout())
		                         .createdAt(utils.getStrDate(cinemaHall.getCreatedAt()))
		                         .updatedAt(utils.getStrDate(cinemaHall.getUpdatedAt()))
		                         .build();
	}

	public CinemaHall mapToCinemaHallEntityFromAddCinemaHallRequest(AddCinemaHallRequest addCinemaHallRequest)
	{
		CinemaHall cinemaHall = new CinemaHall();
		cinemaHall.setName(addCinemaHallRequest.getName());
		cinemaHall.setTotalSeats(addCinemaHallRequest.getTotalSeats());
		cinemaHall.setSeatingLayout(addCinemaHallRequest.getSeatingLayout());
		cinemaHall.setCreatedAt(utils.getCurrentDate());
		cinemaHall.setUpdatedAt(utils.getCurrentDate());
		return cinemaHall;
	}

	public CinemaHall mapToCinemaHallEntityFromUpdateCinemaHallRequest(CinemaHall cinemaHall, UpdateCinemaHallRequest updateCinemaHallRequest)
	{
		if(!ObjectUtils.isEmpty(updateCinemaHallRequest.getName())) {
			cinemaHall.setName(updateCinemaHallRequest.getName());
		}
		if(!ObjectUtils.isEmpty(updateCinemaHallRequest.getTotalSeats())) {
			cinemaHall.setTotalSeats(updateCinemaHallRequest.getTotalSeats());
		}
		if(!ObjectUtils.isEmpty(updateCinemaHallRequest.getSeatingLayout())) {
			cinemaHall.setSeatingLayout(updateCinemaHallRequest.getSeatingLayout());
		}
		cinemaHall.setUpdatedAt(utils.getCurrentDate());
		return cinemaHall;
	}
}
