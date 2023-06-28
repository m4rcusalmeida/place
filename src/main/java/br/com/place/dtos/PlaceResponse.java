package br.com.place.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PlaceResponse {
	private Long id;
	private String name;
	private String slug;
	private String city;
	private String state;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
