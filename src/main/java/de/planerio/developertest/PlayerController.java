package de.planerio.developertest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {

    Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerRepository pRepo;

    @GetMapping("/player")
    public Iterable<Player> getPlayers() {
        logger.info("List all players");
        return pRepo.findAll();
    }

    @PostMapping("/player")
    public Player createPlayer(@RequestBody Player p) {
        logger.info("Add a new player");
        return pRepo.save(p);
    }

    @PostMapping("/player/delete/{playerId}")
    public void deletePlayer(@PathVariable long playerId) {
        logger.info("Delete the player: " + playerId);
        pRepo.deleteById(playerId);
    }
}