package de.planerio.developertest.services;

import de.planerio.developertest.constants.Constants;
import de.planerio.developertest.enums.Position;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.models.Player;
import de.planerio.developertest.repositories.PlayerRepository;
import de.planerio.developertest.repositories.TeamRepository;
import de.planerio.developertest.services.converters.PlayerConverter;
import de.planerio.developertest.services.converters.TeamConverter;
import de.planerio.developertest.services.dtos.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class PlayerService {

    static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Page<PlayerDTO> listPlayers(String position, String onlyDefense, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (onlyDefense != null) {
            logger.info("SERVICE -> List players on defense");
            Set<Position> defensePositions = new HashSet<>();
            defensePositions.add(Position.of("GK"));
            defensePositions.add(Position.of("CB"));
            defensePositions.add(Position.of("RB"));
            defensePositions.add(Position.of("LB"));
            defensePositions.add(Position.of("LWB"));
            defensePositions.add(Position.of("RWB"));
            if (onlyDefense.equals("ASC"))
                return PlayerConverter.fromEntityToDTO(playerRepository.findByPositionIn(defensePositions,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "lastName"))));
            else if (onlyDefense.equals("DESC"))
                return PlayerConverter.fromEntityToDTO(playerRepository.findByPositionIn(defensePositions,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastName"))));
            else
                throw new WrongParameterException();
        } else if (position == null || position.equals("")) {
            logger.info("SERVICE -> List all players");
            return PlayerConverter.fromEntityToDTO(playerRepository.findAll(pageable));
        } else {
            logger.info("SERVICE -> List players by position");
            return PlayerConverter.fromEntityToDTO(playerRepository.findByPosition(Position.of(position), pageable));
        }
    }

    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        logger.info("SERVICE -> Add new player");
        validatePlayerDTO(playerDTO);
        Player player = PlayerConverter.fromDTOToEntity(playerDTO);
        return PlayerConverter.fromEntityToDTO(playerRepository.save(player));
    }

    public PlayerDTO getPlayer(long playerId) {
        logger.info("SERVICE -> Get the player: " + playerId);
        return PlayerConverter.fromEntityToDTO(playerRepository.findById(playerId).orElseThrow(NotFoundException::new));
    }

    public void deletePlayer(long playerId) {
        logger.info("SERVICE -> Delete the player: " + playerId);
        playerRepository.findById(playerId).orElseThrow(NotFoundException::new);
        playerRepository.deleteById(playerId);
    }

    public void updatePlayer(long playerId, PlayerDTO updatedPlayer) {
        logger.info("SERVICE -> Update the player: " + playerId);
        Player currentPlayer = playerRepository.findById(playerId).orElseThrow(NotFoundException::new);
        validatePlayerDTO(updatedPlayer);
        if (updatedPlayer.getName() != null) currentPlayer.setName(updatedPlayer.getFirstName(), updatedPlayer.getLastName());
        playerRepository.save(currentPlayer);
    }

    private void validatePlayerDTO(PlayerDTO playerDTO){
        logger.info("SERVICE -> Check name already exist: " + playerDTO.getName());
        if (playerDTO.getName() != null) {
            Optional<Iterable<Player>> players = playerRepository.findByName(playerDTO.getName());
            if (players.isPresent()) throw new NameAlreadyExistException();
        }
        logger.info("SERVICE -> Check team provided");
        if (playerDTO.getTeam() == null) throw new SchemaInvalidException();
        logger.info("SERVICE -> Check team not found");
        playerDTO.setTeam(TeamConverter
                .fromEntityToDTO(teamRepository
                        .findById(playerDTO.getTeam().getId())
                        .orElseThrow(NotFoundException::new)));
        logger.info("SERVICE -> Check shirt numbers by team");
        if (StreamSupport.stream(TeamConverter
                .fromEntityToDTO(teamRepository
                        .findById(playerDTO.getTeam().getId()).get()).getPlayers().spliterator(), false)
                .anyMatch(p -> p.getShirtNumber() == playerDTO.getShirtNumber())) throw new ShirtNumberByTeamException();
        logger.info("SERVICE -> Check shirt numbers by team");
        Optional<Iterable<Player>> playersOnTheSameTeam = playerRepository.findByTeamId(playerDTO.getTeam().getId());
        if(playersOnTheSameTeam.isPresent()){
            logger.info("SERVICE -> Check the number of players on the team");
            if (StreamSupport.stream(playersOnTheSameTeam.get().spliterator(), false).count() == Constants.PLAYERS_PER_TEAM)
                throw new PlayerByTeamException();
            logger.info("SERVICE -> There are players on the same team");
            if (StreamSupport.stream(playersOnTheSameTeam.get().spliterator(), false)
                    .anyMatch(p -> p.getShirtNumber() == playerDTO.getShirtNumber()))
                throw new ShirtNumberByTeamException();
        }
    }
}
