package com.example.bookMyShow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddBookingRequest
{
	@ApiModelProperty(example = "4x242s53-04z1-3nq5-27al-zcq28e9t813g", required = true)
	@NotBlank(message = "seatId cannot be null or blank")
	private String seatId;

	@ApiModelProperty(example = "6c142c51-08c6-4cb4-94cf-dcd77e4d459d", required = true)
	@NotBlank(message = "showId cannot be null or blank")
	private String showId;
}
