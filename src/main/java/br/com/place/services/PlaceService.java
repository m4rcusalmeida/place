package br.com.place.services;

import java.util.List;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;

public interface PlaceService {

	PlaceResponse save(PlaceRequest placeRequest);

	PlaceResponse update(Long id, PlaceRequest placeRequest);

	List<PlaceResponse> findAll();

	PlaceResponse findById(Long id);

	List<PlaceResponse> findByName(String name);

	void deletePlaceById(Long id);

}
