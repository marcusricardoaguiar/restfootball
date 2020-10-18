package de.planerio.developertest.services.converters;

import de.planerio.developertest.models.League;
import de.planerio.developertest.services.dtos.LeagueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

public abstract class LeagueConverter {

    static final Logger logger = LoggerFactory.getLogger(LeagueConverter.class);

    public static League fromDTOToEntity(LeagueDTO leagueDTO){
        return new League()
                .setId(leagueDTO.getId())
                .setName(leagueDTO.getName())
                .setCountry(CountryConverter.fromDTOToEntity(leagueDTO.getCountry()))
                .setTeams(TeamConverter.fromDTOToEntity(leagueDTO.getTeams()));
    }

    public static LeagueDTO fromEntityToDTO(League league){
        LeagueDTO leagueDTO = new LeagueDTO();
        leagueDTO.setId(league.getId());
        leagueDTO.setName(league.getName());
        leagueDTO.setCountry(CountryConverter.fromEntityToDTO(league.getCountry()));
        leagueDTO.setTeams(TeamConverter.fromEntityToDTO(league.getTeams()));

        return leagueDTO;
    }

    public static Page<LeagueDTO> fromEntityToDTO(Page<League> leagues){
        logger.info("CONVERTER -> from Page<League> to Page<LeagueDTO>");
        return new PageImpl<>(leagues.stream()
                .map(LeagueConverter::fromEntityToDTO)
                .collect(Collectors.toList()));
    }
}
