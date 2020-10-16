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

    static Logger logger = LoggerFactory.getLogger(TeamConverter.class);

    public static Team fromDTOtoEntity(TeamDTO teamDTO){
        Team team = new Team()
                .setId(teamDTO.getId())
                .setName(teamDTO.getName())
                .setLeague(LeagueConverter.fromDTOtoEntity(teamDTO.getLeague()))
                .setPlayers(PlayerConverter.fromDTOtoEntity(teamDTO.getPlayers()));

        return team;
    }

    public static TeamDTO fromEntitytoDTO(Team team){
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setLeague(LeagueConverter.fromEntitytoDTO(team.getLeague()));
        teamDTO.setPlayers(PlayerConverter.fromEntitytoDTO(team.getPlayers()));

        return teamDTO;
    }

    public static List<Team> fromDTOtoEntity(Iterable<TeamDTO> teamsDTO){
        logger.info("CONVERTER -> from Iterable<TeamDTO> to Iterable<Team>");
        List<Team> teams = StreamSupport
                .stream(teamsDTO.spliterator(), false)
                .map(c -> fromDTOtoEntity(c)).collect(Collectors.toList());
        return teams;
    }

    public static Iterable<TeamDTO> fromEntitytoDTO(Iterable<Team> teams){
        logger.info("CONVERTER -> from Iterable<Team> to Iterable<TeamDTO>");
        Iterable<TeamDTO> teamsDTO = StreamSupport
                .stream(teams.spliterator(), false)
                .map(c -> fromEntitytoDTO(c)).collect(Collectors.toList());
        return teamsDTO;
    }

    public static Page<TeamDTO> fromEntitytoDTO(Page<Team> teams){
        logger.info("CONVERTER -> from Page<Team> to Page<TeamDTO>");
        Page<TeamDTO> teamsDTO = new PageImpl<TeamDTO>(teams.stream()
                .map(c -> fromEntitytoDTO(c))
                .collect(Collectors.toList()));
        return teamsDTO;
    }
}
