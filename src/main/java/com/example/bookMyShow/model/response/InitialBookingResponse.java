package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitialBookingResponse
{
	@ApiModelProperty(example = "df2cbbbf-83a8-4e06-9852-8b126166fc8a")
	private String preBookingId;

	private SeatResponse seatResponse;

	private ShowResponse showResponse;
}
