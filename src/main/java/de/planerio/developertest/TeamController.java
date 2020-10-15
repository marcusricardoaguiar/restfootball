package de.planerio.developertest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeamController {

    Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamRepository tRepo; // team repository

    @GetMapping("/team")
    public Iterable<Team> getTeams() {
        logger.info("List all teams");
        return tRepo.findAll();
    }

    @PostMapping("/team")
    public Team createTeam(@RequestBody Team t) {
        logger.info("Add a new team");
        return tRepo.save(t);
    }

    @PostMapping("/team/delete/{teamId}")
    public void deleteTeam(@PathVariable long teamId) {
        logger.info("Delete the team: " + teamId);
        tRepo.deleteById(teamId);
    }
}
