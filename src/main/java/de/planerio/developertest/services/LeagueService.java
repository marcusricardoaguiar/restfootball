package de.planerio.developertest.services;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NotFoundException;
import de.planerio.developertest.models.League;
import de.planerio.developertest.repositories.LeagueRepository;
import de.planerio.developertest.services.converters.LeagueConverter;
import de.planerio.developertest.services.dtos.LeagueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeagueService {

    Logger logger = LoggerFactory.getLogger(LeagueService.class);

    @Autowired
    private LeagueRepository leagueRepository;

    public Page<LeagueDTO> listLeagues(int page, int size) {
        logger.info("SERVICE -> List all leagues");
        Pageable pageable = PageRequest.of(page, size);
        return LeagueConverter.fromEntitytoDTO(leagueRepository.findAll(pageable));
    }

    public LeagueDTO addLeague(LeagueDTO leagueDTO) {
        logger.info("SERVICE -> Add new league");
        Optional<Iterable<League>> leagues = leagueRepository.findByName(leagueDTO.getName());
        if (leagues.isPresent()) throw new NameAlreadyExistException();
        League league = LeagueConverter.fromDTOtoEntity(leagueDTO);
        return LeagueConverter.fromEntitytoDTO(leagueRepository.save(league));
    }

    public LeagueDTO getLeague(long leagueId) {
        logger.info("SERVICE -> Get the league: " + leagueId);
        return LeagueConverter.fromEntitytoDTO(leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new));
    }

    public void deleteLeague(long leagueId) {
        logger.info("SERVICE -> Delete the league: " + leagueId);
        leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new);
        leagueRepository.deleteById(leagueId);
    }

    public void updateLeague(long leagueId, LeagueDTO updatedLeague) {
        logger.info("SERVICE -> Update the league: " + leagueId);
        League currentLeague = leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new);
        //if (updatedLeague.getLanguage() != null) currentLeague.setLanguage(Language.of(updatedLeague.getLanguage()));
        if (updatedLeague.getName() != null) currentLeague.setName(updatedLeague.getName());
        leagueRepository.save(currentLeague);
    }
}
