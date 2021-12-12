package com.example.bookMyShow.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
import com.example.bookMyShow.util.ObjectMapperSingleton;
import com.example.bookMyShow.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@AllArgsConstructor
public class CinemaHallControllerImpl implements CinemaHallController
{
	public static final ObjectMapper OBJECT_MAPPER = ObjectMapperSingleton.getInstance();
	private final CinemaHallRepository cinemaHallRepository;
	private final SeatRepository seatRepository;
	private final Utils utils;

	@Override
	public List<CinemaHallResponse> getAllCinemaHalls()
	{
		List<CinemaHall> cinemaHallList = cinemaHallRepository.findAll();
		List<CinemaHallResponse> cinemaHallResponseList = new ArrayList<>();
		for(CinemaHall cinemaHall : cinemaHallList)
		{
			cinemaHallResponseList.add(mapToCinemaHallResponse(cinemaHall));
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
		return mapToCinemaHallResponse(cinemaHall);
	}

	@Override
	@SneakyThrows
	public CinemaHallResponse addCinemaHall(AddCinemaHallRequest addCinemaHallRequest)
	{
		Integer totalSeats = addCinemaHallRequest.getTotalSeats();
		String layout = addCinemaHallRequest.getSeatingLayout();
		List<List<Integer>> seatingLayout = OBJECT_MAPPER.readValue(layout, new TypeReference<>() {});

		int counter = getSeatsCount(seatingLayout);
		if(counter!=totalSeats)
		{
			utils.throwServiceException("totalSeats not matching with seatingLayout");
		}
		CinemaHall cinemaHall = cinemaHallRepository.save(mapToCinemaHallEntityFromAddCinemaHallRequest(addCinemaHallRequest));
		List<Seat> seatList = getSeatList(cinemaHall, seatingLayout);
		seatRepository.saveAll(seatList);
		return mapToCinemaHallResponse(cinemaHall);
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
		cinemaHall = cinemaHallRepository.save(mapToCinemaHallEntityFromUpdateCinemaHallRequest(cinemaHall, updateCinemaHallRequest));
		return mapToCinemaHallResponse(cinemaHall);
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

	private List<Seat> getSeatList(CinemaHall cinemaHall, List<List<Integer>> seatingLayout)
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

	private int getSeatsCount(List<List<Integer>> seatingLayout)
	{
		int counter = 0;
		for(List<Integer> rowList : seatingLayout)
		{
			counter += rowList.stream().filter(number -> number != 0).count();
		}
		return counter;
	}

	private Seat mapToSeatEntity(CinemaHall cinemaHall, int seatRowNo, int seatColNo, int gridRowNo, int gridColumnNo)
	{
		String seatNo = String.format("R%sC%s", seatRowNo, seatColNo);
		Seat seat = new Seat();
		seat.setCinemaHall(cinemaHall);
		seat.setSeatNo(seatNo);
		seat.setRowNo(gridRowNo);
		seat.setColumnNo(gridColumnNo);
		return seat;
	}

	private CinemaHallResponse mapToCinemaHallResponse(CinemaHall cinemaHall)
	{
		return CinemaHallResponse.builder().cinemaHallId(cinemaHall.getCinemaHallId())
		                         .name(cinemaHall.getName()).totalSeats(cinemaHall.getTotalSeats())
		                         .seatingLayout(cinemaHall.getSeatingLayout())
		                         .createdAt(utils.getStrDate(cinemaHall.getCreatedAt()))
		                         .updatedAt(utils.getStrDate(cinemaHall.getUpdatedAt()))
		                         .build();
	}

	private CinemaHall mapToCinemaHallEntityFromAddCinemaHallRequest(AddCinemaHallRequest addCinemaHallRequest)
	{
		CinemaHall cinemaHall = new CinemaHall();
		cinemaHall.setName(addCinemaHallRequest.getName());
		cinemaHall.setTotalSeats(addCinemaHallRequest.getTotalSeats());
		cinemaHall.setSeatingLayout(addCinemaHallRequest.getSeatingLayout());
		cinemaHall.setCreatedAt(utils.getCurrentDate());
		cinemaHall.setUpdatedAt(utils.getCurrentDate());
		return cinemaHall;
	}

	private CinemaHall mapToCinemaHallEntityFromUpdateCinemaHallRequest(CinemaHall cinemaHall, UpdateCinemaHallRequest updateCinemaHallRequest)
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
