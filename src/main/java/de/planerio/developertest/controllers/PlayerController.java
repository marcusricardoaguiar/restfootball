package de.planerio.developertest.controllers;

import de.planerio.developertest.services.PlayerService;
import de.planerio.developertest.services.dtos.PlayerDTO;
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
public class PlayerController {

    Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Operation(
            description = "List all available players.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The list of players will be provided."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/players")
    public Page<PlayerDTO> getPlayers(@Param(value = "position") String position,
                                      @Param(value = "onlyDefense") Boolean onlyDefense,
                                      @Param(value = "page") Integer page,
                                      @Param(value = "size") Integer size) {
        if (page == null) { page = 0; }
        if (size == null) { size = 10; }
        logger.info("CONTROLLER -> List players - Page: " + page + " Size: " + size);
        return playerService.listPlayers(position, onlyDefense, page, size);
    }

    @Operation(
            description = "Create a new player.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The player was successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The player schema is invalid and therefore the player has not been created."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/player")
    public PlayerDTO createPlayer(@RequestBody PlayerDTO playerDTO) {
        logger.info("CONTROLLER -> Add new player");
        return playerService.addPlayer(playerDTO);
    }

    @Operation(
            description = "Get a specific player.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The player was successfully found."),
            @ApiResponse(responseCode = "400",
                    description = "The playerId was not found or the player schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/player/{playerId}")
    public PlayerDTO getPlayer(@PathVariable long playerId) {
        logger.info("CONTROLLER -> Get the player: " + playerId);
        return playerService.getPlayer(playerId);
    }

    @Operation(
            description = "Remove a specific player.",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The player was successfully removed."),
            @ApiResponse(responseCode = "400",
                    description = "The playerId was not found."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @DeleteMapping("/player/{playerId}")
    public void deletePlayer(@PathVariable long playerId) {
        logger.info("CONTROLLER -> Delete the player: " + playerId);
        playerService.deletePlayer(playerId);
    }

    @Operation(
            description = "Update a specific player.",
            method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The player was successfully updated."),
            @ApiResponse(responseCode = "400",
                    description = "The playerId was not found or the player schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PatchMapping("/player/{playerId}")
    public void updatePlayer(@RequestBody PlayerDTO updatedPlayer, @PathVariable long playerId) {
        logger.info("CONTROLLER -> Update the player: " + playerId);
        playerService.updatePlayer(playerId, updatedPlayer);
    }
}