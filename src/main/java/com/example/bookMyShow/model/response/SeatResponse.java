package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatResponse
{
	@ApiModelProperty(example = "df2cbbbf-83a8-4e06-9852-8b126166fc8a")
	private String seatId;

	@ApiModelProperty(example = "R2C4")
	private String seatNo;
}
