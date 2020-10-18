package de.planerio.developertest.repositories;

import de.planerio.developertest.models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
    Page<Team> findAll(Pageable pageable);
    Optional<Iterable<Team>> findByName(String name);
    Optional<Iterable<Team>> findByLeagueId(Long leagueId);
}
