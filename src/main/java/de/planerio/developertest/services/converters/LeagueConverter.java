package de.planerio.developertest.services.converters;

import de.planerio.developertest.models.League;
import de.planerio.developertest.services.dtos.LeagueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class LeagueConverter {

    static Logger logger = LoggerFactory.getLogger(LeagueConverter.class);

    public static League fromDTOtoEntity(LeagueDTO leagueDTO){
        League league = new League()
                .setId(leagueDTO.getId())
                .setName(leagueDTO.getName())
                .setCountry(CountryConverter.fromDTOtoEntity(leagueDTO.getCountry()))
                .setTeams(TeamConverter.fromDTOtoEntity(leagueDTO.getTeams()));

        return league;
    }

    public static LeagueDTO fromEntitytoDTO(League league){
        LeagueDTO leagueDTO = new LeagueDTO();
        leagueDTO.setId(league.getId());
        leagueDTO.setName(league.getName());
        leagueDTO.setCountry(CountryConverter.fromEntitytoDTO(league.getCountry()));
        leagueDTO.setTeams(TeamConverter.fromEntitytoDTO(league.getTeams()));

        return leagueDTO;
    }

    public static Iterable<LeagueDTO> fromEntitytoDTO(Iterable<League> leagues){
        logger.info("CONVERTER -> from Iterable<League> to Iterable<LeagueDTO>");
        Iterable<LeagueDTO> leaguesDTO = StreamSupport
                .stream(leagues.spliterator(), false)
                .map(c -> fromEntitytoDTO(c)).collect(Collectors.toList());
        return leaguesDTO;
    }

    public static Page<LeagueDTO> fromEntitytoDTO(Page<League> leagues){
        logger.info("CONVERTER -> from Page<League> to Page<LeagueDTO>");
        Page<LeagueDTO> leaguesDTO = new PageImpl<LeagueDTO>(leagues.stream()
                .map(c -> fromEntitytoDTO(c))
                .collect(Collectors.toList()));
        return leaguesDTO;
    }
}
