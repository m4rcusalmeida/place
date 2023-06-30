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
import br.com.place.hateoas.PlaceAssembler;
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
	private final PlaceAssembler placeAssembler;

	@PostMapping
	public ResponseEntity<EntityModel<PlaceResponse>> save(@RequestBody @Valid PlaceRequest placeRequest) {
		return ResponseEntity.ok(placeAssembler.toModel(placeService.save(placeRequest)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<PlaceResponse>> update(@PathVariable Long id,
			@RequestBody @Valid PlaceRequest placeRequest) {
		return ResponseEntity.ok(placeAssembler.toModel(placeService.update(id, placeRequest)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<PlaceResponse>> getPlaceById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.FOUND).body(placeAssembler.toModel(placeService.findById(id)));
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<PlaceResponse>>> getAllPlace(
			@RequestParam(name = "name", required = false) String name) {
		List<PlaceResponse> places;
		if (Objects.nonNull(name)) {
			places = placeService.findByName(name);
		} else {
			places = placeService.findAll();
		}
		return ResponseEntity.status(HttpStatus.FOUND).body(placeAssembler.toCollectionModel(places));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
		placeService.deletePlaceById(id);
		return ResponseEntity.noContent().build();
	}
}
