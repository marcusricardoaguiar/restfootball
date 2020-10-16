package de.planerio.developertest.services.converters;

import de.planerio.developertest.services.dtos.CountryDTO;
import de.planerio.developertest.enums.Language;
import de.planerio.developertest.models.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class CountryConverter {

    static Logger logger = LoggerFactory.getLogger(CountryConverter.class);

    public static Country fromDTOtoEntity(CountryDTO countryDTO){
        Country country = new Country()
                .setId(countryDTO.getId())
                .setName(countryDTO.getName())
                .setLanguage(Language.of(countryDTO.getLanguage()));

        return country;
    }

    public static CountryDTO fromEntitytoDTO(Country country){
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(country.getId());
        countryDTO.setName(country.getName());
        countryDTO.setLanguage(country.getLanguage().getValue());

        return countryDTO;
    }

    public static Iterable<CountryDTO> fromEntitytoDTO(Iterable<Country> countries){
        logger.info("CONVERTER -> from Iterable<Country> to Iterable<CountryDTO>");
        Iterable<CountryDTO> countriesDTO = StreamSupport
                .stream(countries.spliterator(), false)
                .map(c -> fromEntitytoDTO(c)).collect(Collectors.toList());
        return countriesDTO;
    }

    public static Page<CountryDTO> fromEntitytoDTO(Page<Country> countries){
        logger.info("CONVERTER -> from Page<Country> to Page<CountryDTO>");
        Page<CountryDTO> countriesDTO = new PageImpl<CountryDTO>(countries.stream()
                .map(c -> fromEntitytoDTO(c))
                .collect(Collectors.toList()));
        return countriesDTO;
    }
}
