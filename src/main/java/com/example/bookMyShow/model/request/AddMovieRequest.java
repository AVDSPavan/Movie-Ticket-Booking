package com.example.bookMyShow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddMovieRequest
{
	@NotBlank(message = "title cannot be null or blank")
	@ApiModelProperty(example = "Avengers Endgame")
	private String title;

	@ApiModelProperty(example = "Avengers: Endgame is a 2019 American superhero film based on the Marvel Comics superhero team the Avengers")
	private String description;

	@NotBlank(message = "duration cannot be null or blank")
	@ApiModelProperty(example = "150")
	private Integer durationInMinutes;

	@NotBlank(message = "language cannot be null or blank")
	@ApiModelProperty(example = "English")
	private String language;
}
