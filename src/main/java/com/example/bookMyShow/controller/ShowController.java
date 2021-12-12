package com.example.bookMyShow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookMyShow.entity.Seat;
import com.example.bookMyShow.entity.Show;
import com.example.bookMyShow.model.request.AddShowRequest;
import com.example.bookMyShow.model.response.SeatResponse;
import com.example.bookMyShow.model.response.ShowResponse;

@RequestMapping("/show")
public interface ShowController
{
	@GetMapping(path = "/getAll")
	List<ShowResponse> getAllShows();

	@GetMapping(path = "/getAllByMovieTitle")
	List<ShowResponse> getShowsByMovieTitle(@RequestParam("movieTitle") String movieTitle);

	@GetMapping(path = "/getAvailableSeats")
	List<SeatResponse> getAvailableSeats(@RequestParam("id") String showId);

	@GetMapping(path = "/getBookedSeats")
	List<SeatResponse> getBookedSeats(@RequestParam("id") String showId);

	@GetMapping(path = "/get")
	ShowResponse getShow(@RequestParam("id") String showId);

	@PostMapping(path = "/add")
	ShowResponse addShow(@RequestBody AddShowRequest addShowRequest);

	@PutMapping(path = "/update")
	Show updateShow(@RequestBody Show Show);

	@DeleteMapping(path = "/delete")
	void deleteShow(@RequestParam("id") String showId);
}
