package br.com.place.mapper;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.models.Place;

public interface PlaceMapper {

	Place toPlace(PlaceRequest placeRequest);

	PlaceResponse toPlaceResponse(Place place);

}
