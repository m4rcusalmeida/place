package br.com.place.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.mapper.PlaceMapper;
import br.com.place.models.Place;
import br.com.place.repositories.PlaceRepository;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private PlaceMapper placeMapper;

	@InjectMocks
	private PlaceServiceImpl placeService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		placeService = new PlaceServiceImpl(placeRepository, placeMapper);
	}

	@Test
	@DisplayName("Test for Given Place Object when Save Place then Return Place Object")
	public void testGivenPlaceObject_WhenSavePlace_thenReturnPlaceObject() {
		// Given
		PlaceRequest placeRequest = new PlaceRequest();
		given(placeMapper.toPlace(any(PlaceRequest.class))).willReturn(new Place());
		given(placeRepository.save(any(Place.class))).willReturn(new Place());
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(new PlaceResponse());
		// When
		placeService.save(placeRequest);
		// Then
		verify(placeMapper, times(1)).toPlace(any(PlaceRequest.class));
		verify(placeRepository, times(1)).save(any(Place.class));
		verify(placeMapper, times(1)).toPlaceResponse(any(Place.class));

	}

	@Test
	@DisplayName("Test for Given Place Object when Update Place then Return Updated Place")
	public void testGivenPlaceObject_WhenUpdatePlace_thenReturnUpdatedPlace() {
		// Given
		Long id = 1L;
		PlaceRequest placeRequest = new PlaceRequest();
		Place place = new Place();
		given(placeRepository.findById(id)).willReturn(Optional.of(place));
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(new PlaceResponse());
		given(placeRepository.save(any(Place.class))).willReturn(place);
		// When
		placeService.update(id, placeRequest);
		// Then
		verify(placeRepository, times(1)).findById(id);
		verify(placeMapper, times(1)).toPlaceResponse(any(Place.class));
		verify(placeRepository, times(1)).save(any(Place.class));

	}

	@Test
	@DisplayName("Test for Given Place Object when FindAll then Return Place List")
	public void testGivenPlaceObject_WhenFindAll_thenReturnPlaceList() {
		// Given
		List<Place> places = new ArrayList<>();
		places.add(new Place());
		given(placeRepository.findAll()).willReturn(places);
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(new PlaceResponse());
		// When
		placeService.findAll();
		// Then
		verify(placeRepository, times(1)).findAll();
		verify(placeMapper, times(places.size())).toPlaceResponse(any(Place.class));

	}

	@Test
	@DisplayName("Test for Given Place Object when Delete By Place Id")
	public void testGivenPlaceObject_WhenDeleteByPlaceId() {
		// Given
		Long id = 1L;
		Place place = new Place();
		given(placeRepository.findById(id)).willReturn(Optional.of(place));
		// When
		placeService.deletePlaceById(id);
		// Then
		verify(placeRepository, times(1)).findById(id);
		verify(placeRepository, times(1)).delete(any(Place.class));
	}

}
