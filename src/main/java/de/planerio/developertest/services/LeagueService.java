package de.planerio.developertest.services;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.enums.Position;
import de.planerio.developertest.exceptions.LeaguesByCountryException;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NotFoundException;
import de.planerio.developertest.exceptions.TeamByLeagueException;
import de.planerio.developertest.models.League;
import de.planerio.developertest.repositories.CountryRepository;
import de.planerio.developertest.repositories.LeagueRepository;
import de.planerio.developertest.repositories.TeamRepository;
import de.planerio.developertest.services.converters.CountryConverter;
import de.planerio.developertest.services.converters.LeagueConverter;
import de.planerio.developertest.services.converters.PlayerConverter;
import de.planerio.developertest.services.converters.TeamConverter;
import de.planerio.developertest.services.dtos.LeagueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class LeagueService {

    Logger logger = LoggerFactory.getLogger(LeagueService.class);

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Page<LeagueDTO> listLeagues(String language, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (language == null || language.equals("")) {
            logger.info("SERVICE -> List all leagues");
            return LeagueConverter.fromEntitytoDTO(leagueRepository.findAll(pageable));
        }
        logger.info("SERVICE -> List leagues by language");
        return LeagueConverter.fromEntitytoDTO(leagueRepository.findByCountry_Language(Language.of(language), pageable).get());
    }

    public LeagueDTO addLeague(LeagueDTO leagueDTO) {
        logger.info("SERVICE -> Add new league");
        validateLeagueDTO(leagueDTO);
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
        logger.info("SERVICE -> Check leagues by each country");
        Optional<Iterable<League>> leaguesByCountry = leagueRepository.findByCountryId(leagueDTO.getCountry().getId());
        if(leaguesByCountry.isPresent()){
            logger.info("SERVICE -> There are leagues with this country");
            if (StreamSupport.stream(leaguesByCountry.get().spliterator(), false)
                    .filter(l -> l.getId() != leagueDTO.getId())
                    .count() > 0)
                throw new LeaguesByCountryException();
        }
        logger.info("SERVICE -> Check league not found");
        leagueDTO.setCountry(CountryConverter
                .fromEntitytoDTO(countryRepository
                        .findById(leagueDTO.getCountry().getId())
                        .orElseThrow(NotFoundException::new)));
        logger.info("SERVICE -> Check number of teams by league");
        if (StreamSupport.stream(leagueDTO.getTeams().spliterator(), false)
                .count() > 20) throw new TeamByLeagueException();
        logger.info("SERVICE -> Check team not found");
        leagueDTO.getTeams().forEach(t -> t = TeamConverter
                .fromEntitytoDTO(teamRepository
                        .findById(t.getId())
                        .orElseThrow(NotFoundException::new)));
    }
}
