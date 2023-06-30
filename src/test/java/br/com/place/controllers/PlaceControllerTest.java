package br.com.place.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.hateoas.PlaceAssembler;
import br.com.place.services.PlaceService;

@WebMvcTest
@ComponentScan(basePackageClasses = PlaceAssembler.class)
public class PlaceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private PlaceAssembler placeAssembler;

	@MockBean
	private PlaceService placeService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Test Given Place Object When Create Place then Return Saved Place")
	public void testGivenPlaceObject_WhenCreatePlace_thenReturnSavedPlace() throws JsonProcessingException, Exception {
		// Given
		PlaceRequest placeRequest = PlaceRequest.builder().name("test").slug("test").city("test").state("test").build();
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("test").slug("test").city("test")
				.state("test").build();
		given(placeService.save(any(PlaceRequest.class))).willReturn(placeResponse);
		given(placeAssembler.toModel(placeResponse)).willReturn(EntityModel.of(placeResponse));
		// When
		mockMvc.perform(post("/api/places").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(placeRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value(placeRequest.getName()))
				.andExpect(jsonPath("$.slug").value(placeRequest.getSlug()))
				.andExpect(jsonPath("$.city").value(placeRequest.getCity()))
				.andExpect(jsonPath("$.state").value(placeRequest.getState())).andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._links.self[2].type").value("DELETE"));
	}

	@Test
	@DisplayName("Test Given Place Id When findById Place then Return Place Object")
	public void testGivenPlaceId_WhenFindById_thenReturnPlaceObject() throws JsonProcessingException, Exception {
		// Given
		Long id = 1L;
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("test").slug("test").city("test")
				.state("test").build();
		given(placeService.findById(id)).willReturn(placeResponse);
		given(placeAssembler.toModel(placeResponse)).willReturn(EntityModel.of(placeResponse));
		// When
		mockMvc.perform(get("/api/places/{id}", id)).andExpect(status().isFound()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value(placeResponse.getName()))
				.andExpect(jsonPath("$.slug").value(placeResponse.getSlug()))
				.andExpect(jsonPath("$.city").value(placeResponse.getCity()))
				.andExpect(jsonPath("$.state").value(placeResponse.getState())).andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._links.self[2].type").value("DELETE"));
	}

	@Test
	@DisplayName("Given List Of Places When Find All Places Without Query then Return Places List")
	public void testGivenListOfPlaces_WhenFindAllPlacesWithoutQuery_thenReturnPlacesList()
			throws JsonProcessingException, Exception {
		// Given
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("test").slug("test").city("test")
				.state("test").build();
		PlaceResponse placeResponse2 = PlaceResponse.builder().id(2L).name("test").slug("test").city("test")
				.state("test").build();
		List<PlaceResponse> placeResponses = Arrays.asList(placeResponse, placeResponse2);

		// When
		given(placeService.findAll()).willReturn(placeResponses);
		given(placeAssembler.toCollectionModel(placeResponses)).willReturn(
				CollectionModel.of(Arrays.asList(EntityModel.of(placeResponse), EntityModel.of(placeResponse2))));
		// Then
		mockMvc.perform(get("/api/places")).andExpect(status().isFound())
				.andExpect(jsonPath("$._embedded.places").isArray())
				.andExpect(jsonPath("$._embedded.places.length()").value(placeResponses.size()))
				.andExpect(jsonPath("$._embedded.places[0].id").value(placeResponses.get(0).getId()))
				.andExpect(jsonPath("$._embedded.places[0].name").value(placeResponses.get(0).getName()))
				.andExpect(jsonPath("$._embedded.places[0].slug").value(placeResponses.get(0).getSlug()))
				.andExpect(jsonPath("$._embedded.places[0].city").value(placeResponses.get(0).getCity()))
				.andExpect(jsonPath("$._embedded.places[0].state").value(placeResponses.get(0).getState()))
				.andExpect(jsonPath("$._embedded.places[0]._links.self").isArray())
				.andExpect(jsonPath("$._embedded.places[0]._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._embedded.places[0]._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._embedded.places[0]._links.self[2].type").value("DELETE"))
				.andExpect(jsonPath("$._embedded.places[1].id").value(placeResponses.get(1).getId()))
				.andExpect(jsonPath("$._embedded.places[1].name").value(placeResponses.get(1).getName()))
				.andExpect(jsonPath("$._embedded.places[1].slug").value(placeResponses.get(1).getSlug()))
				.andExpect(jsonPath("$._embedded.places[1].city").value(placeResponses.get(1).getCity()))
				.andExpect(jsonPath("$._embedded.places[1].state").value(placeResponses.get(1).getState()))
				.andExpect(jsonPath("$._embedded.places[1]._links.self").isArray())
				.andExpect(jsonPath("$._embedded.places[1]._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._embedded.places[1]._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._embedded.places[1]._links.self[2].type").value("DELETE"));
	}

	@Test
	@DisplayName("Given List Of Places When Find All Places With Query then Return Places List")
	public void testGivenListOfPlaces_WhenFindAllPlacesWithQuery_thenReturnPlacesList()
			throws JsonProcessingException, Exception {
		// Given
		List<PlaceResponse> placeResponses = new ArrayList<>();
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("test").slug("test").city("test")
				.state("test").build();
		PlaceResponse placeResponse2 = PlaceResponse.builder().id(2L).name("test").slug("test").city("test")
				.state("test").build();
		placeResponses = Arrays.asList(placeResponse, placeResponse2);
		// When
		given(placeService.findByName(anyString())).willReturn(placeResponses);
		given(placeAssembler.toCollectionModel(placeResponses)).willReturn(
				CollectionModel.of(Arrays.asList(EntityModel.of(placeResponse), EntityModel.of(placeResponse2))));
		// Then
		mockMvc.perform(get("/api/places").param("name", "tes")).andExpect(status().isFound())
				.andExpect(jsonPath("$._embedded.places").isArray())
				.andExpect(jsonPath("$._embedded.places.length()").value(placeResponses.size()))
				.andExpect(jsonPath("$._embedded.places[0].id").value(placeResponses.get(0).getId()))
				.andExpect(jsonPath("$._embedded.places[0].name").value(placeResponses.get(0).getName()))
				.andExpect(jsonPath("$._embedded.places[0].slug").value(placeResponses.get(0).getSlug()))
				.andExpect(jsonPath("$._embedded.places[0].city").value(placeResponses.get(0).getCity()))
				.andExpect(jsonPath("$._embedded.places[0].state").value(placeResponses.get(0).getState()))
				.andExpect(jsonPath("$._embedded.places[0]._links.self").isArray())
				.andExpect(jsonPath("$._embedded.places[0]._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._embedded.places[0]._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._embedded.places[0]._links.self[2].type").value("DELETE"))
				.andExpect(jsonPath("$._embedded.places[1].id").value(placeResponses.get(1).getId()))
				.andExpect(jsonPath("$._embedded.places[1].name").value(placeResponses.get(1).getName()))
				.andExpect(jsonPath("$._embedded.places[1].slug").value(placeResponses.get(1).getSlug()))
				.andExpect(jsonPath("$._embedded.places[1].city").value(placeResponses.get(1).getCity()))
				.andExpect(jsonPath("$._embedded.places[1].state").value(placeResponses.get(1).getState()))
				.andExpect(jsonPath("$._embedded.places[1]._links.self").isArray())
				.andExpect(jsonPath("$._embedded.places[1]._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._embedded.places[1]._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._embedded.places[1]._links.self[2].type").value("DELETE"));

	}

	@Test
	@DisplayName("Test Given Update Place When update Place then Return Updated Place")
	public void testGivenUpdatePlace_WhenUpdate_thenReturnUpdatedPlaceObject()
			throws JsonProcessingException, Exception {
		// Given
		Long id = 1L;
		PlaceResponse placeResponse = PlaceResponse.builder().id(1L).name("test").slug("test").city("test")
				.state("test").build();
		PlaceRequest placeRequest = PlaceRequest.builder().name("test1").slug("test1").city("test1").state("test1")
				.build();
		PlaceResponse updatedPlaceResponse = PlaceResponse.builder().id(1L).name("test1").slug("test1").city("test1")
				.state("test1").build();
		given(placeService.findById(id)).willReturn(placeResponse);
		given(placeService.update(id, placeRequest)).willReturn(updatedPlaceResponse);
		given(placeAssembler.toModel(placeResponse)).willReturn(EntityModel.of(placeResponse));
		// When
		mockMvc.perform(put("/api/places/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPlaceResponse))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value(updatedPlaceResponse.getName()))
				.andExpect(jsonPath("$.slug").value(updatedPlaceResponse.getSlug()))
				.andExpect(jsonPath("$.city").value(updatedPlaceResponse.getCity()))
				.andExpect(jsonPath("$.state").value(updatedPlaceResponse.getState()))
				.andExpect(jsonPath("$._links.self").isArray())
				.andExpect(jsonPath("$._links.self[0].type").value("GET"))
				.andExpect(jsonPath("$._links.self[1].type").value("PUT"))
				.andExpect(jsonPath("$._links.self[2].type").value("DELETE"));
	}

	@Test
	@DisplayName("Test Given Place Id When delete then Return No Content")
	public void testGivenPlaceId_WhenDelete_thenReturnNoContent() throws JsonProcessingException, Exception {
		// Given
		Long id = 1L;
		willDoNothing().given(placeService).deletePlaceById(id);
		// When
		mockMvc.perform(delete("/api/places/{id}", id)).andExpect(status().isNoContent());
	}
}
