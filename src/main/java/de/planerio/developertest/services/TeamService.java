package de.planerio.developertest.services;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NotFoundException;
import de.planerio.developertest.models.Team;
import de.planerio.developertest.repositories.TeamRepository;
import de.planerio.developertest.services.converters.TeamConverter;
import de.planerio.developertest.services.dtos.TeamDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    Logger logger = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    private TeamRepository teamRepository;

    public Page<TeamDTO> listTeams(int page, int size) {
        logger.info("SERVICE -> List all teams");
        Pageable pageable = PageRequest.of(page, size);
        return TeamConverter.fromEntitytoDTO(teamRepository.findAll(pageable));
    }

    public TeamDTO addTeam(TeamDTO teamDTO) {
        logger.info("SERVICE -> Add new team");
        Optional<Iterable<Team>> teams = teamRepository.findByName(teamDTO.getName());
        if (teams.isPresent()) throw new NameAlreadyExistException();
        Team team = TeamConverter.fromDTOtoEntity(teamDTO);
        return TeamConverter.fromEntitytoDTO(teamRepository.save(team));
    }

    public TeamDTO getTeam(long teamId) {
        logger.info("SERVICE -> Get the team: " + teamId);
        return TeamConverter.fromEntitytoDTO(teamRepository.findById(teamId).orElseThrow(NotFoundException::new));
    }

    public void deleteTeam(long teamId) {
        logger.info("SERVICE -> Delete the team: " + teamId);
        teamRepository.findById(teamId).orElseThrow(NotFoundException::new);
        teamRepository.deleteById(teamId);
    }

    public void updateTeam(long teamId, TeamDTO updatedTeam) {
        logger.info("SERVICE -> Update the team: " + teamId);
        Team currentTeam = teamRepository.findById(teamId).orElseThrow(NotFoundException::new);
        //if (updatedTeam.getLanguage() != null) currentTeam.setLanguage(Language.of(updatedTeam.getLanguage()));
        if (updatedTeam.getName() != null) currentTeam.setName(updatedTeam.getName());
        teamRepository.save(currentTeam);
    }
}
