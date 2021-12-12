package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import com.example.bookMyShow.entity.BookingStatus;

@Getter
@Builder
public class BookingResponse
{
	@ApiModelProperty(example = "df2cbbbf-83a8-4e06-9852-8b126166fc8a")
	private String bookingId;

	private BookingStatus bookingStatus;

	private SeatResponse seatResponse;

	private ShowResponse showResponse;
}
