package de.planerio.developertest.services;

import de.planerio.developertest.enums.Position;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.models.League;
import de.planerio.developertest.models.Player;
import de.planerio.developertest.models.Team;
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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class PlayerService {

    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Page<PlayerDTO> listPlayers(String position, Boolean onlyDefense, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if ((onlyDefense == null || onlyDefense == false) && (position == null || position.equals(""))) {
            logger.info("SERVICE -> List all players");
            return PlayerConverter.fromEntitytoDTO(playerRepository.findAll(pageable));
        } else if (onlyDefense != null && onlyDefense == true){
            logger.info("SERVICE -> List players on defense");
            Set<Position> defensePositions = new HashSet<>();
            defensePositions.add(Position.of("GK"));
            defensePositions.add(Position.of("CB"));
            defensePositions.add(Position.of("RB"));
            defensePositions.add(Position.of("LB"));
            defensePositions.add(Position.of("LWB"));
            defensePositions.add(Position.of("RWB"));
            return PlayerConverter.fromEntitytoDTO(playerRepository.findByPositionIn(defensePositions, pageable).get());
        } else {
            logger.info("SERVICE -> List players by position");
            return PlayerConverter.fromEntitytoDTO(playerRepository.findByPosition(Position.of(position), pageable).get());
        }
    }

    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        logger.info("SERVICE -> Add new player");
        validatePlayerDTO(playerDTO);
        Player player = PlayerConverter.fromDTOtoEntity(playerDTO);
        return PlayerConverter.fromEntitytoDTO(playerRepository.save(player));
    }

    public PlayerDTO getPlayer(long playerId) {
        logger.info("SERVICE -> Get the player: " + playerId);
        return PlayerConverter.fromEntitytoDTO(playerRepository.findById(playerId).orElseThrow(NotFoundException::new));
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
        if (updatedPlayer.getName() != null) currentPlayer.setName(updatedPlayer.getName());
        playerRepository.save(currentPlayer);
    }

    private void validatePlayerDTO(PlayerDTO playerDTO){
        logger.info("SERVICE -> Check name already exist: " + playerDTO.getName());
        if (playerDTO.getName() != null) {
            Optional<Iterable<Player>> players = playerRepository.findByName(playerDTO.getName());
            if (players.isPresent()) throw new NameAlreadyExistException();
        }
        logger.info("SERVICE -> Check team not found");
        playerDTO.setTeam(TeamConverter
                .fromEntitytoDTO(teamRepository
                        .findById(playerDTO.getTeam().getId())
                        .orElseThrow(NotFoundException::new)));
        logger.info("SERVICE -> Check shirt numbers by team");
        if (StreamSupport.stream(TeamConverter
                .fromEntitytoDTO(teamRepository
                        .findById(playerDTO.getTeam().getId()).get()).getPlayers().spliterator(), false)
                .filter(p -> p.getShirtNumber() == playerDTO.getShirtNumber()).count() > 0) throw new ShirtNumberByTeamException();
        logger.info("SERVICE -> Check shirt numbers by team");
        Optional<Iterable<Player>> playersOnTheSameTeam = playerRepository.findByTeamId(playerDTO.getTeam().getId());
        if(playersOnTheSameTeam.isPresent()){
            logger.info("SERVICE -> There are players on the same team");
            if (StreamSupport.stream(playersOnTheSameTeam.get().spliterator(), false)
                    .filter(p -> p.getShirtNumber() == playerDTO.getShirtNumber())
                    .count() > 0)
                throw new ShirtNumberByTeamException();
        }
    }
}
