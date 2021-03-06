package com.example.bookMyShow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateCinemaHallRequest
{
	@NotBlank(message = "cinemaHallId cannot be null or blank")
	@ApiModelProperty(example = "156et682gyd78u1bsd")
	private String cinemaHallId;

	@ApiModelProperty(example = "Apsara")
	@NotBlank(message = "name cannot be null or blank")
	private String name;

	@ApiModelProperty(example = "14")
	@NotBlank(message = "totalSeats cannot be null or blank")
	private Integer totalSeats;

	@ApiModelProperty(example = "[[1,1,1,1,1],[0,0,1,0,0],[1,1,1,1,1]]")
	@NotBlank(message = "seatingLayout cannot be null or blank")
	private String seatingLayout;
}
