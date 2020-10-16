package de.planerio.developertest.services.converters;

import de.planerio.developertest.enums.Position;
import de.planerio.developertest.models.Player;
import de.planerio.developertest.services.dtos.PlayerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class PlayerConverter {

    static Logger logger = LoggerFactory.getLogger(PlayerConverter.class);

    public static Player fromDTOtoEntity(PlayerDTO playerDTO){
        Player player = new Player()
                .setId(playerDTO.getId())
                .setName(playerDTO.getName())
                .setPosition(Position.of(playerDTO.getPosition()))
                .setShirtNumber(playerDTO.getShirtNumber())
                .setTeam(TeamConverter.fromDTOtoEntity(playerDTO.getTeam()));

        return player;
    }

    public static PlayerDTO fromEntitytoDTO(Player player){
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setPosition(player.getPosition().getValue());
        playerDTO.setShirtNumber(player.getShirtNumber());
        playerDTO.setTeam(TeamConverter.fromEntitytoDTO(player.getTeam()));

        return playerDTO;
    }

    public static List<Player> fromDTOtoEntity(Iterable<PlayerDTO> playersDTO){
        logger.info("CONVERTER -> from Iterable<PlayerDTO> to Iterable<Player>");
        List<Player> players = StreamSupport
                .stream(playersDTO.spliterator(), false)
                .map(c -> fromDTOtoEntity(c)).collect(Collectors.toList());
        return players;
    }

    public static Iterable<PlayerDTO> fromEntitytoDTO(Iterable<Player> players){
        logger.info("CONVERTER -> from Iterable<Player> to Iterable<PlayerDTO>");
        Iterable<PlayerDTO> playersDTO = StreamSupport
                .stream(players.spliterator(), false)
                .map(c -> fromEntitytoDTO(c)).collect(Collectors.toList());
        return playersDTO;
    }

    public static Page<PlayerDTO> fromEntitytoDTO(Page<Player> players){
        logger.info("CONVERTER -> from Page<Player> to Page<PlayerDTO>");
        Page<PlayerDTO> playersDTO = new PageImpl<PlayerDTO>(players.stream()
                .map(c -> fromEntitytoDTO(c))
                .collect(Collectors.toList()));
        return playersDTO;
    }
}
