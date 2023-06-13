package br.com.place.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.slugify.Slugify;

import br.com.place.dtos.PlaceRequest;
import br.com.place.dtos.PlaceResponse;
import br.com.place.exceptions.PlaceNotFoundException;
import br.com.place.mapper.PlaceMapper;
import br.com.place.models.Place;
import br.com.place.repositories.PlaceRepository;
import br.com.place.services.PlaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

	private final PlaceRepository placeRepository;
	private final PlaceMapper placeMapper;
	private final Slugify slg = Slugify.builder().build();

	@Override
	@Transactional
	public PlaceResponse save(PlaceRequest placeRequest) {
		placeRequest.setSlug(slg.slugify(placeRequest.getSlug()));
		return placeMapper.toPlaceResponse(placeRepository.save(placeMapper.toPlace(placeRequest)));
	}

	@Override
	@Transactional
	public PlaceResponse update(Long id, PlaceRequest placeRequest) {
		placeRequest.setSlug(slg.slugify(placeRequest.getSlug()));
		Place place = getPlaceById(id);
		BeanUtils.copyProperties(placeRequest, place);
		return placeMapper.toPlaceResponse(placeRepository.save(place));
	}

	@Override
	public List<PlaceResponse> findAll() {
		return placeRepository.findAll().stream().map(placeMapper::toPlaceResponse).collect(Collectors.toList());
	}

	@Override
	public PlaceResponse findById(Long id) {
		return placeMapper.toPlaceResponse(getPlaceById(id));
	}

	@Override
	public List<PlaceResponse> findByName(String name) {
		return placeRepository.findByNameContainingIgnoreCase(name).stream().map(placeMapper::toPlaceResponse)
				.collect(Collectors.toList());
	}

	private Place getPlaceById(Long id) {
		return placeRepository.findById(id)
				.orElseThrow(() -> new PlaceNotFoundException("Place with id " + id + " not found!"));
	}

	@Override
	public void deletePlaceById(Long id) {
		Place place = getPlaceById(id);
		placeRepository.delete(place);
	}

}
