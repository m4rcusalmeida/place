package br.com.place.mapper.impl;

import org.springframework.stereotype.Component;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.mapper.PlaceMapper;
import br.com.place.models.Place;

@Component
public class PlaceMapperImpl implements PlaceMapper {

	@Override
	public Place toPlace(PlaceRequest placeRequest) {

		return Place.builder().name(placeRequest.getName()).slug(placeRequest.getSlug()).city(placeRequest.getCity())
				.state(placeRequest.getState()).build();
	}

	@Override
	public PlaceResponse toPlaceResponse(Place place) {
		return PlaceResponse.builder().id(place.getId()).name(place.getName()).slug(place.getSlug())
				.city(place.getCity()).state(place.getState()).createdAt(place.getCreatedAt())
				.updatedAt(place.getUpdatedAt()).build();
	}

}
