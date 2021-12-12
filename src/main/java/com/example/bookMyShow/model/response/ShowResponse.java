package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowResponse
{
	@ApiModelProperty(example = "Avengers")
	private String movieTitle;

	@ApiModelProperty(example = "Apsara theatre")
	private String cinemaHallName;

	@ApiModelProperty(example = "09:00:00")
	private String startTime;

	@ApiModelProperty(example = "11:45:00")
	private String endTime;

	@ApiModelProperty(example = "65EFYDVGxd1wbdkfkj")
	private String showId;

	@ApiModelProperty(example = "65EFYDVGxd1wbdkfkj")
	private String movieId;

	@ApiModelProperty(example = "65EFYDVGxd1wbdkfkj")
	private String cinemaHallId;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String createdAt;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String updatedAt;
}
