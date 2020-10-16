package de.planerio.developertest.repositories;

import de.planerio.developertest.models.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LeagueRepository extends PagingAndSortingRepository<League, Long> {
    Page<League> findAll(Pageable pageable);
    Optional<Iterable<League>> findByName(String name);
}
