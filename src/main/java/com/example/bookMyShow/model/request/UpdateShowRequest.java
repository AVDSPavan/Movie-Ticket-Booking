package com.example.bookMyShow.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateShowRequest
{
	@NotBlank(message = "date cannot be null")
	private Date date;

	@NotBlank(message = "startTime cannot be null")
	private Date startTime;

	@NotBlank(message = "endTime cannot be null")
	private Date endTime;

	@NotBlank(message = "movieId cannot be null or blank")
	private String movieId;

	@NotBlank(message = "cinemaHallId cannot be null or blank")
	private String cinemaHallId;
}
