package de.planerio.developertest.services;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NotFoundException;
import de.planerio.developertest.models.Country;
import de.planerio.developertest.repositories.CountryRepository;
import de.planerio.developertest.services.converters.CountryConverter;
import de.planerio.developertest.services.dtos.CountryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {

    static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryRepository countryRepository;

    public Page<CountryDTO> listCountries(int page, int size) {
        logger.info("SERVICE -> List all countries");
        Pageable pageable = PageRequest.of(page, size);
        return CountryConverter.fromEntityToDTO(countryRepository.findAll(pageable));
    }

    public CountryDTO addCountry(CountryDTO countryDTO) {
        logger.info("SERVICE -> Add new country");
        Optional<Iterable<Country>> countries = countryRepository.findByName(countryDTO.getName());
        if (countries.isPresent()) throw new NameAlreadyExistException();
        Country country = CountryConverter.fromDTOToEntity(countryDTO);
        return CountryConverter.fromEntityToDTO(countryRepository.save(country));
    }

    public CountryDTO getCountry(long countryId) {
        logger.info("SERVICE -> Get the country: " + countryId);
        return CountryConverter.fromEntityToDTO(countryRepository.findById(countryId).orElseThrow(NotFoundException::new));
    }

    public void deleteCountry(long countryId) {
        logger.info("SERVICE -> Delete the country: " + countryId);
        countryRepository.findById(countryId).orElseThrow(NotFoundException::new);
        countryRepository.deleteById(countryId);
    }

    public void updateCountry(long countryId, CountryDTO updatedCountry) {
        logger.info("SERVICE -> Update the country: " + countryId);
        Country currentCountry = countryRepository.findById(countryId).orElseThrow(NotFoundException::new);
        if (updatedCountry.getLanguage() != null) currentCountry.setLanguage(Language.of(updatedCountry.getLanguage()));
        if (updatedCountry.getName() != null) currentCountry.setName(updatedCountry.getName());
        countryRepository.save(currentCountry);
    }
}
