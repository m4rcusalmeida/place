package br.com.place.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.exceptions.PlaceNotFoundException;
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
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("teste").slug("teste").city("teste")
				.state("teste").build();
		Place place = new Place();
		BeanUtils.copyProperties(placeResponse, place);
		place.setId(1L);
		given(placeRepository.findById(anyLong())).willReturn(Optional.of(place));
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(placeResponse);
		given(placeRepository.save(any(Place.class))).willReturn(place);
		// When
		PlaceResponse updatePlace = placeService.update(id, new PlaceRequest());
		// Then
		Assertions.assertNotNull(updatePlace);
		Assertions.assertEquals(place.getId(), updatePlace.getId());
		verify(placeRepository, times(1)).findById(id);
		verify(placeMapper, times(1)).toPlaceResponse(any(Place.class));
		verify(placeRepository, times(1)).save(any(Place.class));

	}

	@Test
	@DisplayName("Test for Given Place List when FindAll then Return Place List")
	public void testGivenPlaceList_WhenFindAll_thenReturnPlaceList() {
		// Given
		List<Place> places = new ArrayList<>();
		Place place1 = Place.builder().id(1L).name("teste1").slug("teste1").city("teste1").state("teste1").build();
		Place place2 = Place.builder().id(2L).name("teste2").slug("teste2").city("teste2").state("teste2").build();
		places.add(place1);
		places.add(place2);
		given(placeRepository.findAll()).willReturn(places);
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(new PlaceResponse());
		// When
		List<PlaceResponse> placeList = placeService.findAll();
		// Then
		verify(placeRepository, times(1)).findAll();
		verify(placeMapper, times(placeList.size())).toPlaceResponse(any(Place.class));
		Assertions.assertNotNull(placeList);
		Assertions.assertEquals(2, placeList.size());
	}

	@Test
	@DisplayName("Test for Given Place List when FindAll then Return Empty Place List")
	public void testGivenEmptyPlaceList_WhenFindAll_thenReturnEmptyPlaceList() {
		// Given
		given(placeRepository.findAll()).willReturn(Collections.emptyList());
		// When
		List<PlaceResponse> placeList = placeService.findAll();
		// Then
		verify(placeRepository, times(1)).findAll();
		Assertions.assertTrue(placeList.isEmpty());
		Assertions.assertEquals(0, placeList.size());
	}

	@Test
	@DisplayName("Test for Given Place Object when FindByName then Return Place List")
	public void testGivenPlaceObject_WhenFindByName_thenReturnPlaceList() {
		// Given
		var name = "test";
		List<Place> places = new ArrayList<>();
		places.add(new Place());
		given(placeRepository.findByNameContainingIgnoreCase(name)).willReturn(places);
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(new PlaceResponse());
		// When
		placeService.findByName(name);
		// Then
		verify(placeRepository, times(1)).findByNameContainingIgnoreCase(name);
		verify(placeMapper, times(places.size())).toPlaceResponse(any(Place.class));

	}

	@Test
	@DisplayName("Test for Given Place Object when Not GetPlaceById then ThrowsException")
	public void testGivenPlaceObject_WhenNotGetPlaceById_thenThrowsException() {
		// Given
		Long id = 1L;
		var expected = "Place with id " + id + " not found!";
		given(placeRepository.findById(id)).willReturn(Optional.empty());

		// When
		PlaceNotFoundException assertThrows = Assertions.assertThrows(PlaceNotFoundException.class, () -> {
			placeService.findById(id);
		});
		// Then
		verify(placeRepository, times(1)).findById(id);
		Assertions.assertEquals(expected, assertThrows.getMessage());

	}

	@Test
	@DisplayName("Test for Given Place Object when GetPlaceById then Return Place")
	public void testGivenPlaceObject_WhenGetPlaceById_thenReturnPlace() {
		// Given

		Place place = Place.builder().id(1L).name("teste1").slug("teste1").city("teste1").state("teste1").build();
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("teste1").slug("teste1").city("teste1")
				.state("teste1").build();
		given(placeRepository.findById(anyLong())).willReturn(Optional.of(place));
		given(placeMapper.toPlaceResponse(any(Place.class))).willReturn(placeResponse);
		// When
		PlaceResponse placeFind = placeService.findById(place.getId());
		// Then
		verify(placeRepository, times(1)).findById(place.getId());
		verify(placeMapper, times(1)).toPlaceResponse(place);
		Assertions.assertNotNull(placeFind);
		Assertions.assertEquals(place.getId(), placeFind.getId());
	}

	@Test
	@DisplayName("Test for Given Place Object when Delete By Place Id")
	public void testGivenPlaceObject_WhenDeleteByPlaceId() {
		// Given
		Place place = new Place();
		place.setId(1L);
		given(placeRepository.findById(anyLong())).willReturn(Optional.of(place));
		willDoNothing().given(placeRepository).delete(place);
		// When
		placeService.deletePlaceById(place.getId());
		// Then
		verify(placeRepository, times(1)).findById(place.getId());
		verify(placeRepository, times(1)).delete(any(Place.class));
	}

}
