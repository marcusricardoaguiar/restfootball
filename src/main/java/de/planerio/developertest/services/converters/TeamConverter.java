package de.planerio.developertest.services.converters;

import de.planerio.developertest.models.Team;
import de.planerio.developertest.services.dtos.TeamDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class TeamConverter {

    static final Logger logger = LoggerFactory.getLogger(TeamConverter.class);

    public static Team fromDTOToEntity(TeamDTO teamDTO){
        return new Team()
                .setId(teamDTO.getId())
                .setName(teamDTO.getName())
                .setLeague(LeagueConverter.fromDTOToEntity(teamDTO.getLeague()))
                .setPlayers(PlayerConverter.fromDTOToEntity(teamDTO.getPlayers()));
    }

    public static TeamDTO fromEntityToDTO(Team team){
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setLeague(LeagueConverter.fromEntityToDTO(team.getLeague()));
        teamDTO.setPlayers(PlayerConverter.fromEntityToDTO(team.getPlayers()));

        return teamDTO;
    }

    public static List<Team> fromDTOToEntity(Iterable<TeamDTO> teamsDTO){
        logger.info("CONVERTER -> from Iterable<TeamDTO> to Iterable<Team>");
        return StreamSupport
                .stream(teamsDTO.spliterator(), false)
                .map(TeamConverter::fromDTOToEntity).collect(Collectors.toList());
    }

    public static Iterable<TeamDTO> fromEntityToDTO(Iterable<Team> teams){
        logger.info("CONVERTER -> from Iterable<Team> to Iterable<TeamDTO>");
        return StreamSupport
                .stream(teams.spliterator(), false)
                .map(TeamConverter::fromEntityToDTO).collect(Collectors.toList());
    }

    public static Page<TeamDTO> fromEntityToDTO(Page<Team> teams){
        logger.info("CONVERTER -> from Page<Team> to Page<TeamDTO>");
        return new PageImpl<>(teams.stream()
                .map(TeamConverter::fromEntityToDTO)
                .collect(Collectors.toList()));
    }
}
