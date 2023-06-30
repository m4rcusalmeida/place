package br.com.place.hateoas;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

import br.com.place.controllers.PlaceController;
import br.com.place.dtos.PlaceResponse;

@Component
public class PlaceAssembler implements SimpleRepresentationModelAssembler<PlaceResponse> {

	@Override
	public void addLinks(EntityModel<PlaceResponse> resource) {
		Long id = resource.getContent().getId();
		Link selfLink = linkTo(methodOn(PlaceController.class).getPlaceById(id)).withSelfRel().withType("GET");
		Link putLink = linkTo(methodOn(PlaceController.class).update(id, null)).withSelfRel().withType("PUT");
		Link deleteLink = linkTo(methodOn(PlaceController.class).deletePlace(id)).withSelfRel().withType("DELETE");
		resource.add(selfLink, putLink, deleteLink);
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<PlaceResponse>> resources) {
		Link saveLink = linkTo(methodOn(PlaceController.class).save(null)).withSelfRel().withType("POST");
		Link selfLink = linkTo(methodOn(PlaceController.class).getAllPlace(null)).withSelfRel().withType("GET");
		resources.add(selfLink, saveLink);

	}

}
