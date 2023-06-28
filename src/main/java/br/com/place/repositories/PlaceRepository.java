package br.com.place.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.place.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
	List<Place> findByNameContainingIgnoreCase(String keyword);

}
