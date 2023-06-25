package br.com.place.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.place.models.Place;

@DataJpaTest
class PlaceRepositoryTest {

	@Autowired
	private PlaceRepository placeRepository;

	@Test
	@DisplayName("Given Place Object when Save then Return Saved Place")
	public void testGivenPlaceObject_whenSave_thenReturnSavedPlace() {
		// Given
		Place place = Place.builder().name("teste").slug("teste").city("teste").state("teste").build();
		// When
		Place savedPlace = placeRepository.save(place);
		// Then
		assertNotNull(savedPlace);
		assertTrue(savedPlace.getId() > 0);
		assertEquals(place.getName(), savedPlace.getName());
		assertEquals(place.getSlug(), savedPlace.getSlug());
		assertEquals(place.getCity(), savedPlace.getCity());
		assertEquals(place.getState(), savedPlace.getState());

	}

	@Test
	@DisplayName("Given Place Object when FindById then Return Place Object")
	public void testGivenPlaceObject_whenFindById_thenReturnPlaceObject() {
		// Given
		Place place = Place.builder().name("teste").slug("teste").city("teste").state("teste").build();
		placeRepository.save(place);
		// When
		Place findById = placeRepository.findById(place.getId()).get();
		// Then
		assertNotNull(findById);
		assertEquals(place.getId(), findById.getId());

	}

	@Test
	@DisplayName("Given Place Object when Delete then Remove Place")
	public void testGivenPlaceObject_whenDeleteById_thenRemovePlaceObject() {
		// Given
		Place place = Place.builder().name("teste").slug("teste").city("teste").state("teste").build();
		placeRepository.save(place);
		// When
		placeRepository.deleteById(place.getId());
		Optional<Place> placeOptional = placeRepository.findById(place.getId());
		// Then
		assertTrue(placeOptional.isEmpty());

	}

	@Test
	@DisplayName("Given Place Object when Update Place then Return Updated Place Object")
	public void testGivenPlaceObject_whenUpdatePlace_thenReturnUpdatedPlaceObject() {
		// Given
		Place place = Place.builder().name("test").slug("test").city("test").state("test").build();
		placeRepository.save(place);
		// When
		Place findById = placeRepository.findById(place.getId()).get();
		findById.setName("updated object");
		findById.setSlug("updated object");
		findById.setCity("updated object");
		findById.setState("updated object");
		Place updatedPlace = placeRepository.save(findById);
		// Then
		assertNotNull(updatedPlace);
		assertEquals("updated object", updatedPlace.getName());
		assertEquals("updated object", updatedPlace.getSlug());
		assertEquals("updated object", updatedPlace.getCity());
		assertEquals("updated object", updatedPlace.getState());

	}

	@Test
	@DisplayName("Given Place List when FindAll then Return Place List")
	public void testGivenPlaceList_whenFindAll_thenReturnPlaceList() {
		// Given
		Place place = Place.builder().name("teste").slug("teste").city("teste").state("teste").build();
		Place place2 = Place.builder().name("teste2").slug("teste2").city("teste2").state("teste2").build();
		Place savedPlace = placeRepository.save(place);
		Place savedPlace2 = placeRepository.save(place2);
		// When
		List<Place> placeList = placeRepository.findAll();
		// Then
		assertNotNull(placeList);
		assertEquals(2, placeList.size());
	}

}
