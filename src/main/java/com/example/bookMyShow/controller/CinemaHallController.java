package com.example.bookMyShow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookMyShow.model.request.AddCinemaHallRequest;
import com.example.bookMyShow.model.request.UpdateCinemaHallRequest;
import com.example.bookMyShow.model.response.CinemaHallResponse;

@RequestMapping("/cinemaHall")
public interface CinemaHallController
{
	@GetMapping(path = "/getAll")
	List<CinemaHallResponse> getAllCinemaHalls();

	@GetMapping(path = "/get")
	CinemaHallResponse getCinemaHall(@RequestParam("id") String cinemaHallId);

	@PostMapping(path = "/add")
	CinemaHallResponse addCinemaHall(@RequestBody AddCinemaHallRequest addCinemaHallRequest);

	@PutMapping(path = "/update")
	CinemaHallResponse updateCinemaHall(@RequestBody UpdateCinemaHallRequest updateCinemaHallRequest);

	@DeleteMapping(path = "/delete")
	void deleteCinemaHall(@RequestParam("id") String cinemaHallId);
}
