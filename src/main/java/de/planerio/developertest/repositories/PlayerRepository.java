package de.planerio.developertest.repositories;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.enums.Position;
import de.planerio.developertest.models.League;
import de.planerio.developertest.models.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {
    Page<Player> findAll(Pageable pageable);
    Optional<Iterable<Player>> findByName(String name);
    Optional<Iterable<Player>> findByTeamId(Long teamId);
    Optional<Page<Player>> findByPosition(Position position, Pageable pageable);
    Optional<Page<Player>> findByPositionIn(Set<Position> position, Pageable pageable);
}