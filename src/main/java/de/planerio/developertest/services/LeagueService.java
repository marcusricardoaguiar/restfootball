package de.planerio.developertest.services;

import de.planerio.developertest.constants.Constants;
import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.models.League;
import de.planerio.developertest.repositories.CountryRepository;
import de.planerio.developertest.repositories.LeagueRepository;
import de.planerio.developertest.services.converters.CountryConverter;
import de.planerio.developertest.services.converters.LeagueConverter;
import de.planerio.developertest.services.dtos.LeagueDTO;
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
public class LeagueService {

    static final Logger logger = LoggerFactory.getLogger(LeagueService.class);

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private CountryRepository countryRepository;

    public Page<LeagueDTO> listLeagues(String language, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (language == null || language.equals("")) {
            logger.info("SERVICE -> List all leagues");
            return LeagueConverter.fromEntityToDTO(leagueRepository.findAll(pageable));
        }
        logger.info("SERVICE -> List leagues by language");
        return LeagueConverter.fromEntityToDTO(leagueRepository.findByCountry_Language(Language.of(language), pageable));
    }

    public LeagueDTO addLeague(LeagueDTO leagueDTO) {
        logger.info("SERVICE -> Add new league");
        validateLeagueDTO(leagueDTO);
        League league = LeagueConverter.fromDTOToEntity(leagueDTO);
        return LeagueConverter.fromEntityToDTO(leagueRepository.save(league));
    }

    public LeagueDTO getLeague(long leagueId) {
        logger.info("SERVICE -> Get the league: " + leagueId);
        return LeagueConverter.fromEntityToDTO(leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new));
    }

    public void deleteLeague(long leagueId) {
        logger.info("SERVICE -> Delete the league: " + leagueId);
        leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new);
        leagueRepository.deleteById(leagueId);
    }

    public void updateLeague(long leagueId, LeagueDTO updatedLeague) {
        logger.info("SERVICE -> Update the league: " + leagueId);
        League currentLeague = leagueRepository.findById(leagueId).orElseThrow(NotFoundException::new);
        validateLeagueDTO(updatedLeague);
        if (updatedLeague.getName() != null) currentLeague.setName(updatedLeague.getName());
        leagueRepository.save(currentLeague);
    }

    private void validateLeagueDTO(LeagueDTO leagueDTO){
        logger.info("SERVICE -> Check name already exist: " + leagueDTO.getName());
        if (leagueDTO.getName() != null) {
            Optional<Iterable<League>> leagues = leagueRepository.findByName(leagueDTO.getName());
            if (leagues.isPresent()) throw new NameAlreadyExistException();
        }
        logger.info("SERVICE -> Check country provided");
        if (leagueDTO.getCountry() == null) throw new SchemaInvalidException();
        logger.info("SERVICE -> Check leagues by each country");
        Optional<Iterable<League>> leaguesByCountry = leagueRepository.findByCountryId(leagueDTO.getCountry().getId());
        if(leaguesByCountry.isPresent()){
            logger.info("SERVICE -> There are leagues with this country");
            if (StreamSupport.stream(leaguesByCountry.get().spliterator(), false)
                    .anyMatch(l -> l.getId() != leagueDTO.getId()))
                throw new LeaguesByCountryException();
        }
        logger.info("SERVICE -> Check country not found");
        leagueDTO.setCountry(CountryConverter
                .fromEntityToDTO(countryRepository
                        .findById(leagueDTO.getCountry().getId())
                        .orElseThrow(NotFoundException::new)));
        logger.info("SERVICE -> Check number of teams by league");
        if (leagueDTO.getTeams() == null) leagueDTO.setTeams(new ArrayList<>());
        if (StreamSupport.stream(leagueDTO.getTeams().spliterator(), false)
                .count() > Constants.TEAMS_PER_LEAGUE) throw new TeamByLeagueException();
    }
}
