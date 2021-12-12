package com.example.bookMyShow.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieResponse
{
	@ApiModelProperty(example = "156et682gyd78u1bsd")
	private String movieId;

	@ApiModelProperty(example = "Avengers Endgame")
	private String title;

	@ApiModelProperty(example = "Avengers: Endgame is a 2019 American superhero film based on the Marvel Comics superhero team the Avengers")
	private String description;

	@ApiModelProperty(example = "150")
	private Integer durationInMinutes;

	@ApiModelProperty(example = "English")
	private String language;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String createdAt;

	@ApiModelProperty(example = "2021-12-12 16:27:54")
	private String updatedAt;
}
