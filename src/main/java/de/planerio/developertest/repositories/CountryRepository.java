package de.planerio.developertest.repositories;

import de.planerio.developertest.models.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {
    Page<Country> findAll(Pageable pageable);
    Optional<Iterable<Country>> findByName(String name);
}
