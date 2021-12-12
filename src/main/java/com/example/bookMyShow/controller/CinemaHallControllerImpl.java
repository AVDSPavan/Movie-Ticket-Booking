package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;
import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.model.request.AddCinemaHallRequest;
import com.example.bookMyShow.model.request.UpdateCinemaHallRequest;
import com.example.bookMyShow.model.response.CinemaHallResponse;
import com.example.bookMyShow.repository.CinemaHallRepository;
import com.example.bookMyShow.repository.SeatRepository;
import com.example.bookMyShow.service.CinemaHallService;
import com.example.bookMyShow.util.ObjectMapperSingleton;
import com.example.bookMyShow.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@AllArgsConstructor
public class CinemaHallControllerImpl implements CinemaHallController
{
	public static final ObjectMapper OBJECT_MAPPER = ObjectMapperSingleton.getInstance();
	private final CinemaHallRepository cinemaHallRepository;
	private final CinemaHallService cinemaHallService;
	private final SeatRepository seatRepository;
	private final Utils utils;

	@Override
	public List<CinemaHallResponse> getAllCinemaHalls()
	{
		List<CinemaHall> cinemaHallList = cinemaHallRepository.findAll();
		List<CinemaHallResponse> cinemaHallResponseList = new ArrayList<>();
		for(CinemaHall cinemaHall : cinemaHallList)
		{
			cinemaHallResponseList.add(cinemaHallService.mapToCinemaHallResponse(cinemaHall));
		}
		return cinemaHallResponseList;
	}

	@Override
	@SneakyThrows
	public CinemaHallResponse getCinemaHall(String cinemaHallId)
	{
		CinemaHall cinemaHall = cinemaHallRepository.findByCinemaHallId(cinemaHallId);
		if(ObjectUtils.isEmpty(cinemaHall))
		{
			String message = String.format("No cinemaHall found with id: %s", cinemaHall.getCinemaHallId());
			utils.throwServiceException(message);
		}
		return cinemaHallService.mapToCinemaHallResponse(cinemaHall);
	}

	@Override
	@SneakyThrows
	public CinemaHallResponse addCinemaHall(AddCinemaHallRequest addCinemaHallRequest)
	{
		Integer totalSeats = addCinemaHallRequest.getTotalSeats();
		String layout = addCinemaHallRequest.getSeatingLayout();
		List<List<Integer>> seatingLayout = OBJECT_MAPPER.readValue(layout, new TypeReference<>() {});

		int counter = cinemaHallService.getSeatsCount(seatingLayout);
		if(counter!=totalSeats)
		{
			utils.throwServiceException("totalSeats not matching with seatingLayout");
		}
		CinemaHall cinemaHall = cinemaHallRepository.save(cinemaHallService.mapToCinemaHallEntityFromAddCinemaHallRequest(addCinemaHallRequest));
		List<Seat> seatList = cinemaHallService.getSeatList(cinemaHall, seatingLayout);
		seatRepository.saveAll(seatList);
		return cinemaHallService.mapToCinemaHallResponse(cinemaHall);
	}

	@Override
	@SneakyThrows
	public CinemaHallResponse updateCinemaHall(UpdateCinemaHallRequest updateCinemaHallRequest)
	{
		String cinemaHallId = updateCinemaHallRequest.getCinemaHallId();
		CinemaHall cinemaHall = cinemaHallRepository.findByCinemaHallId(cinemaHallId);
		if(ObjectUtils.isEmpty(cinemaHall))
		{
			String message = String.format("CinemaHall not found with id: %s", cinemaHallId);
			utils.throwServiceException(message);
		}
		cinemaHall = cinemaHallRepository.save(cinemaHallService.mapToCinemaHallEntityFromUpdateCinemaHallRequest(cinemaHall, updateCinemaHallRequest));
		return cinemaHallService.mapToCinemaHallResponse(cinemaHall);
	}

	@Override
	@SneakyThrows
	public void deleteCinemaHall(String cinemaHallId)
	{
		CinemaHall cinemaHall = cinemaHallRepository.findByCinemaHallId(cinemaHallId);
		if(ObjectUtils.isEmpty(cinemaHall))
		{
			String message = String.format("CinemaHall not found with id: %s", cinemaHallId);
			utils.throwServiceException(message);
		}
		cinemaHallRepository.delete(cinemaHall);
	}
}
