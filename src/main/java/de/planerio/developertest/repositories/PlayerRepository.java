package de.planerio.developertest.repositories;

import de.planerio.developertest.models.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {
    Page<Player> findAll(Pageable pageable);
    Optional<Iterable<Player>> findByName(String name);
}