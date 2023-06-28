package br.com.place.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.services.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;

	@PostMapping
	public ResponseEntity<EntityModel<PlaceResponse>> save(@RequestBody @Valid PlaceRequest placeRequest) {
		PlaceResponse placeResponse = placeService.save(placeRequest);
		Link selfLink = WebMvcLinkBuilder.linkTo(PlaceController.class).slash(placeResponse.getId()).withSelfRel();
		EntityModel<PlaceResponse> model = EntityModel.of(placeResponse, selfLink);
		return ResponseEntity.ok(model);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<PlaceResponse>> update(@PathVariable Long id,
			@RequestBody @Valid PlaceRequest placeRequest) {
		PlaceResponse updatedResponse = placeService.update(id, placeRequest);
		Link selfLink = WebMvcLinkBuilder.linkTo(PlaceController.class).slash(updatedResponse.getId()).withSelfRel();
		EntityModel<PlaceResponse> model = EntityModel.of(updatedResponse, selfLink);
		return ResponseEntity.ok(model);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<PlaceResponse>> getPlaceById(@PathVariable Long id) {
		PlaceResponse placeResponse = placeService.findById(id);
		Link selfLink = WebMvcLinkBuilder.linkTo(PlaceController.class).slash(placeResponse.getId()).withSelfRel();
		EntityModel<PlaceResponse> model = EntityModel.of(placeResponse, selfLink);
		return ResponseEntity.status(HttpStatus.FOUND).body(model);
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<PlaceResponse>>> getAllPlace(
			@RequestParam(name = "q", required = false) String q) {
		List<PlaceResponse> places;
		if (Objects.nonNull(q)) {
			places = placeService.findByName(q);
		} else {
			places = placeService.findAll();
		}
		Link selfLink = WebMvcLinkBuilder.linkTo(PlaceController.class).withSelfRel();
		List<EntityModel<PlaceResponse>> placeResources = places.stream()
				.map(place -> EntityModel.of(place,
						WebMvcLinkBuilder
								.linkTo(WebMvcLinkBuilder.methodOn(PlaceController.class).getPlaceById(place.getId()))
								.withSelfRel()))
				.collect(Collectors.toList());
		CollectionModel<EntityModel<PlaceResponse>> collectionModel = CollectionModel.of(placeResources, selfLink);
		return ResponseEntity.status(HttpStatus.FOUND).body(collectionModel);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
		placeService.deletePlaceById(id);
		return ResponseEntity.noContent().build();
	}
}
