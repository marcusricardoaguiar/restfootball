package de.planerio.developertest.services;

import de.planerio.developertest.constants.Constants;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.models.Team;
import de.planerio.developertest.repositories.LeagueRepository;
import de.planerio.developertest.repositories.TeamRepository;
import de.planerio.developertest.services.converters.LeagueConverter;
import de.planerio.developertest.services.converters.TeamConverter;
import de.planerio.developertest.services.dtos.TeamDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TeamService {

    static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    public Page<TeamDTO> listTeams(int page, int size) {
        logger.info("SERVICE -> List all teams");
        Pageable pageable = PageRequest.of(page, size);
        return TeamConverter.fromEntityToDTO(teamRepository.findAll(pageable));
    }

    public TeamDTO addTeam(TeamDTO teamDTO) {
        logger.info("SERVICE -> Add new team");
        validateTeamDTO(teamDTO);
        Team team = TeamConverter.fromDTOToEntity(teamDTO);
        return TeamConverter.fromEntityToDTO(teamRepository.save(team));
    }

    public TeamDTO getTeam(long teamId) {
        logger.info("SERVICE -> Get the team: " + teamId);
        return TeamConverter.fromEntityToDTO(teamRepository.findById(teamId).orElseThrow(NotFoundException::new));
    }

    public void deleteTeam(long teamId) {
        logger.info("SERVICE -> Delete the team: " + teamId);
        teamRepository.findById(teamId).orElseThrow(NotFoundException::new);
        teamRepository.deleteById(teamId);
    }

    public void updateTeam(long teamId, TeamDTO updatedTeam) {
        logger.info("SERVICE -> Update the team: " + teamId);
        Team currentTeam = teamRepository.findById(teamId).orElseThrow(NotFoundException::new);
        validateTeamDTO(updatedTeam);
        if (updatedTeam.getName() != null) currentTeam.setName(updatedTeam.getName());
        teamRepository.save(currentTeam);
    }

    private void validateTeamDTO(TeamDTO teamDTO){
        logger.info("SERVICE -> Check name already exist: " + teamDTO.getName());
        if (teamDTO.getName() != null) {
            Optional<Iterable<Team>> teams = teamRepository.findByName(teamDTO.getName());
            if (teams.isPresent()) throw new NameAlreadyExistException();
        }
        logger.info("SERVICE -> Check league provided");
        if (teamDTO.getLeague() == null) throw new SchemaInvalidException();
        logger.info("SERVICE -> Check league not found");
        teamDTO.setLeague(LeagueConverter
                .fromEntityToDTO(leagueRepository
                        .findById(teamDTO.getLeague().getId())
                        .orElseThrow(NotFoundException::new)));
        logger.info("SERVICE -> Check limit of teams per league");
        Optional<Iterable<Team>> teamsOnTheSameLeague = teamRepository.findByLeagueId(teamDTO.getLeague().getId());
        if(teamsOnTheSameLeague.isPresent()){
            logger.info("SERVICE -> Check the number of teams on the league");
            if (StreamSupport.stream(teamsOnTheSameLeague.get().spliterator(), false).count() == Constants.TEAMS_PER_LEAGUE)
                throw new TeamByLeagueException();
        }
        logger.info("SERVICE -> Check number of players by team");
        if (teamDTO.getPlayers() == null) teamDTO.setPlayers(new ArrayList<>());
    }
}
