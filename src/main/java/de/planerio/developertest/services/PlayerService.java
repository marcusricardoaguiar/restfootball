package de.planerio.developertest.services;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NotFoundException;
import de.planerio.developertest.models.Player;
import de.planerio.developertest.repositories.PlayerRepository;
import de.planerio.developertest.services.converters.PlayerConverter;
import de.planerio.developertest.services.dtos.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    public Page<PlayerDTO> listPlayers(int page, int size) {
        logger.info("SERVICE -> List all players");
        Pageable pageable = PageRequest.of(page, size);
        return PlayerConverter.fromEntitytoDTO(playerRepository.findAll(pageable));
    }

    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        logger.info("SERVICE -> Add new player");
        Optional<Iterable<Player>> players = playerRepository.findByName(playerDTO.getName());
        if (players.isPresent()) throw new NameAlreadyExistException();
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
        //if (updatedPlayer.getLanguage() != null) currentPlayer.setLanguage(Language.of(updatedPlayer.getLanguage()));
        if (updatedPlayer.getName() != null) currentPlayer.setName(updatedPlayer.getName());
        playerRepository.save(currentPlayer);
    }
}
