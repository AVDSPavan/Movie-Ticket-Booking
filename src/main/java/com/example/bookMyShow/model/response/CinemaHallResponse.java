package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CinemaHallResponse
{
	@ApiModelProperty(example = "156et682gyd78u1bsd")
	private String cinemaHallId;

	@ApiModelProperty(example = "Apsara")
	private String name;

	@ApiModelProperty(example = "14")
	private Integer totalSeats;

	@ApiModelProperty(example = "[[1,1,1,1,1],[0,0,1,0,0],[1,1,1,1,1]]")
	private String seatingLayout;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String createdAt;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String updatedAt;
}
