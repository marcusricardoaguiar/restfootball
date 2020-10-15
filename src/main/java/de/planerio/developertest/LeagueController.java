package de.planerio.developertest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LeagueController {

    Logger logger = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    private LeagueRepository lRepo; // league repository

    @GetMapping("/league")
    public Iterable<League> getLeagues() {
        logger.info("List all leagues");
        return lRepo.findAll();
    }

    @PostMapping("/league")
    public League createLeague(@RequestBody League l) {
        logger.info("Create a new League");
        return lRepo.save(l);
    }

    @PostMapping("/league/delete/{leagueId}")
    public void deleteLeague(@PathVariable long leagueId) {
        logger.info("Delete the League: " + leagueId);
        lRepo.deleteById(leagueId);
    }
}
