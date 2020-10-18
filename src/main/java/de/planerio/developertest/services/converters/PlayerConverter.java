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

    static final Logger logger = LoggerFactory.getLogger(PlayerConverter.class);

    public static Player fromDTOToEntity(PlayerDTO playerDTO){
        return new Player()
                .setId(playerDTO.getId())
                .setName(playerDTO.getFirstName(), playerDTO.getLastName())
                .setPosition(Position.of(playerDTO.getPosition()))
                .setShirtNumber(playerDTO.getShirtNumber())
                .setTeam(TeamConverter.fromDTOToEntity(playerDTO.getTeam()));
    }

    public static PlayerDTO fromEntityToDTO(Player player){
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getFirstName(), player.getLastName());
        playerDTO.setPosition(player.getPosition().getValue());
        playerDTO.setShirtNumber(player.getShirtNumber());
        playerDTO.setTeam(TeamConverter.fromEntityToDTO(player.getTeam()));

        return playerDTO;
    }

    public static List<Player> fromDTOToEntity(Iterable<PlayerDTO> playersDTO){
        logger.info("CONVERTER -> from Iterable<PlayerDTO> to Iterable<Player>");
        return StreamSupport
                .stream(playersDTO.spliterator(), false)
                .map(PlayerConverter::fromDTOToEntity).collect(Collectors.toList());
    }

    public static Iterable<PlayerDTO> fromEntityToDTO(Iterable<Player> players){
        logger.info("CONVERTER -> from Iterable<Player> to Iterable<PlayerDTO>");
        return StreamSupport
                .stream(players.spliterator(), false)
                .map(PlayerConverter::fromEntityToDTO).collect(Collectors.toList());
    }

    public static Page<PlayerDTO> fromEntityToDTO(Page<Player> players){
        logger.info("CONVERTER -> from Page<Player> to Page<PlayerDTO>");
        return new PageImpl<>(players.stream()
                .map(PlayerConverter::fromEntityToDTO)
                .collect(Collectors.toList()));
    }
}
