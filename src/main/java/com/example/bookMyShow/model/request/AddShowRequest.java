package com.example.bookMyShow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import com.example.bookMyShow.util.validator.DateValidator;

@Getter
@Setter
public class AddShowRequest
{
	@ApiModelProperty(example = "2021-12-12 06:55:04", required = true, notes = "yyyy-MM-dd hh:mm:ss")
	@NotBlank(message = "startTime cannot be null")
	@DateValidator
	private String startTime;

	@ApiModelProperty(example = "65EFYDVGxd1wbdkfkj", required = true)
	@NotBlank(message = "movieId cannot be null or blank")
	private String movieId;

	@ApiModelProperty(example = "65EFYDVGxd1wbdkfkj", required = true)
	@NotBlank(message = "cinemaHallId cannot be null or blank")
	private String cinemaHallId;
}
