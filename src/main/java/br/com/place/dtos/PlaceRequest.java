package br.com.place.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlaceRequest {
	@NotEmpty(message = "must not be empty!")
	@NotNull(message = "must not be null!")
	private String name;
	@NotEmpty(message = "must not be empty!")
	@NotNull(message = "must not be null!")
	private String slug;
	@NotEmpty(message = "must not be empty!")
	@NotNull(message = "must not be null!")
	private String city;
	@NotEmpty(message = "must not be empty!")
	@NotNull(message = "must not be null!")
	private String state;
}
