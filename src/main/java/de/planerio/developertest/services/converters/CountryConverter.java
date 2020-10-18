package de.planerio.developertest.services.converters;

import de.planerio.developertest.services.dtos.CountryDTO;
import de.planerio.developertest.enums.Language;
import de.planerio.developertest.models.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;

public abstract class CountryConverter {

    static final Logger logger = LoggerFactory.getLogger(CountryConverter.class);

    public static Country fromDTOToEntity(CountryDTO countryDTO){
        return new Country()
                .setId(countryDTO.getId())
                .setName(countryDTO.getName())
                .setLanguage(Language.of(countryDTO.getLanguage()));
    }

    public static CountryDTO fromEntityToDTO(Country country){
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(country.getId());
        countryDTO.setName(country.getName());
        countryDTO.setLanguage(country.getLanguage().getValue());

        return countryDTO;
    }

    public static Page<CountryDTO> fromEntityToDTO(Page<Country> countries){
        logger.info("CONVERTER -> from Page<Country> to Page<CountryDTO>");
        return new PageImpl<>(countries.stream()
                .map(CountryConverter::fromEntityToDTO)
                .collect(Collectors.toList()));
    }
}
