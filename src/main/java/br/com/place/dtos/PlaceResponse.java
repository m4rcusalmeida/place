package br.com.place.dtos;

import java.time.LocalDateTime;

import org.springframework.hateoas.server.core.Relation;

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
@Relation(collectionRelation = "places")
public class PlaceResponse {
	private Long id;
	private String name;
	private String slug;
	private String city;
	private String state;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
