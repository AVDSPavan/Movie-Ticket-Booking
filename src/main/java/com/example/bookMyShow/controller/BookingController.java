package com.example.bookMyShow.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookMyShow.entity.Booking;
import com.example.bookMyShow.model.request.AddBookingRequest;
import com.example.bookMyShow.model.response.BookingResponse;
import com.example.bookMyShow.model.response.InitialBookingResponse;

@RequestMapping("/booking")
public interface BookingController
{
	@GetMapping(path = "/getAll")
	List<BookingResponse> getAllBookings();

	@GetMapping(path = "/get")
	BookingResponse getBooking(@RequestParam("id") String bookingId);

	@PostMapping(path = "/add")
	InitialBookingResponse addBooking(@RequestBody @Validated AddBookingRequest addBookingRequest);

	@PostMapping(path = "/complete")
	BookingResponse completeBooking(@RequestParam("preBookingId") String preBookingId);

	@PutMapping(path = "/update")
	BookingResponse updateBooking(@RequestBody Booking booking);

	@DeleteMapping(path = "/delete")
	void deleteBooking(@RequestParam("id") String bookingId);
}
