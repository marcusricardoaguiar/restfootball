package de.planerio.developertest.controllers;

import de.planerio.developertest.services.TeamService;
import de.planerio.developertest.services.dtos.TeamDTO;
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
public class TeamController {

    static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamService teamService;

    @Operation(
            description = "List all available teams.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The list of teams will be provided."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/teams")
    public Page<TeamDTO> getTeams(@Param(value = "page") Integer page,
                                  @Param(value = "size") Integer size) {
        if (page == null) { page = 0; }
        if (size == null) { size = 10; }
        logger.info("CONTROLLER -> List teams - Page: " + page + " Size: " + size);
        return teamService.listTeams(page, size);
    }

    @Operation(
            description = "Create a new team.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The team was successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The team schema is invalid and therefore the team has not been created."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/team")
    public TeamDTO createTeam(@RequestBody TeamDTO teamDTO) {
        logger.info("CONTROLLER -> Add new team");
        return teamService.addTeam(teamDTO);
    }

    @Operation(
            description = "Get a specific team.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The team was successfully found."),
            @ApiResponse(responseCode = "400",
                    description = "The teamId was not found or the team schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/team/{teamId}")
    public TeamDTO getTeam(@PathVariable long teamId) {
        logger.info("CONTROLLER -> Get the team: " + teamId);
        return teamService.getTeam(teamId);
    }

    @Operation(
            description = "Remove a specific team.",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The team was successfully removed."),
            @ApiResponse(responseCode = "400",
                    description = "The teamId was not found."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @DeleteMapping("/team/{teamId}")
    public void deleteTeam(@PathVariable long teamId) {
        logger.info("CONTROLLER -> Delete the team: " + teamId);
        teamService.deleteTeam(teamId);
    }

    @Operation(
            description = "Update a specific team.",
            method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The team was successfully updated."),
            @ApiResponse(responseCode = "400",
                    description = "The teamId was not found or the team schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PatchMapping("/team/{teamId}")
    public void updateTeam(@RequestBody TeamDTO updatedTeam, @PathVariable long teamId) {
        logger.info("CONTROLLER -> Update the team: " + teamId);
        teamService.updateTeam(teamId, updatedTeam);
    }
}
