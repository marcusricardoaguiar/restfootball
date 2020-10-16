package de.planerio.developertest.controllers;

import de.planerio.developertest.services.LeagueService;
import de.planerio.developertest.services.dtos.LeagueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
public class LeagueController {

    Logger logger = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    private LeagueService leagueService;

    @Operation(
            description = "List all available leagues.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The list of leagues will be provided."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/leagues")
    public Page<LeagueDTO> getLeagues(@Param(value = "page") int page,
                                         @Param(value = "size") int size) {
        logger.info("CONTROLLER -> List all leagues");
        return leagueService.listLeagues(page, size);
    }

    @Operation(
            description = "Create a new league.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The league was successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The league schema is invalid and therefore the league has not been created."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/league")
    public LeagueDTO createLeague(@RequestBody LeagueDTO leagueDTO) {
        logger.info("CONTROLLER -> Add new league");
        return leagueService.addLeague(leagueDTO);
    }

    @Operation(
            description = "Get a specific league.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The league was successfully found."),
            @ApiResponse(responseCode = "400",
                    description = "The leagueId was not found or the league schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/league/{leagueId}")
    public LeagueDTO getLeague(@PathVariable long leagueId) {
        logger.info("CONTROLLER -> Get the league: " + leagueId);
        return leagueService.getLeague(leagueId);
    }

    @Operation(
            description = "Remove a specific league.",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The league was successfully removed."),
            @ApiResponse(responseCode = "400",
                    description = "The leagueId was not found."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @DeleteMapping("/league/{leagueId}")
    public void deleteLeague(@PathVariable long leagueId) {
        logger.info("CONTROLLER -> Delete the league: " + leagueId);
        leagueService.deleteLeague(leagueId);
    }

    @Operation(
            description = "Update a specific league.",
            method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The league was successfully updated."),
            @ApiResponse(responseCode = "400",
                    description = "The leagueId was not found or the league schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PatchMapping("/league/{leagueId}")
    public void updateLeague(@RequestBody LeagueDTO updatedLeague, @PathVariable long leagueId) {
        logger.info("CONTROLLER -> Update the league: " + leagueId);
        leagueService.updateLeague(leagueId, updatedLeague);
    }
}
