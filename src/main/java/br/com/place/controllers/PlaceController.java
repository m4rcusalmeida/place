package br.com.place.controllers;

import java.util.List;
import java.util.Objects;

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

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;

	@PostMapping
	public ResponseEntity<PlaceResponse> save(@RequestBody @Valid PlaceRequest placeRequest) {
		return ResponseEntity.ok(placeService.save(placeRequest));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PlaceResponse> update(@PathVariable Long id, @RequestBody @Valid PlaceRequest placeRequest) {
		return ResponseEntity.status(HttpStatus.FOUND).body(placeService.update(id, placeRequest));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.FOUND).body(placeService.findById(id));
	}

	@GetMapping
	public ResponseEntity<List<PlaceResponse>> getAllPlace(@RequestParam(name = "q", required = false) String q) {
		if (Objects.nonNull(q)) {
			return ResponseEntity.status(HttpStatus.FOUND).body(placeService.findByName(q));
		}
		return ResponseEntity.status(HttpStatus.FOUND).body(placeService.findAll());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlace(@PathVariable Long id){
		placeService.deletePlaceById(id);
		return ResponseEntity.noContent().build();
	}
}
