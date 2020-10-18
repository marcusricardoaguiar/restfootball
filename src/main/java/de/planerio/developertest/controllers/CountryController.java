package de.planerio.developertest.controllers;

import de.planerio.developertest.services.CountryService;
import de.planerio.developertest.services.dtos.CountryDTO;
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
public class CountryController {

    static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    @Operation(
            description = "List all available countries.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "The list of countries will be provided."),
            @ApiResponse(responseCode = "409",
                         description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/countries")
    public Page<CountryDTO> getCountries(@Param(value = "page") Integer page,
                                         @Param(value = "size") Integer size) {
        if (page == null) { page = 0; }
        if (size == null) { size = 10; }
        logger.info("CONTROLLER -> List countries - Page: " + page + " Size: " + size);
        return countryService.listCountries(page, size);
    }

    @Operation(
            description = "Create a new country.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The country was successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The country schema is invalid and therefore the country has not been created."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/country")
    public CountryDTO createCountry(@RequestBody CountryDTO countryDTO) {
        logger.info("CONTROLLER -> Add new country");
        return countryService.addCountry(countryDTO);
    }

    @Operation(
            description = "Get a specific country.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The country was successfully found."),
            @ApiResponse(responseCode = "400",
                    description = "The countryId was not found or the country schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/country/{countryId}")
    public CountryDTO getCountry(@PathVariable long countryId) {
        logger.info("CONTROLLER -> Get the country: " + countryId);
        return countryService.getCountry(countryId);
    }

    @Operation(
            description = "Remove a specific country.",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The country was successfully removed."),
            @ApiResponse(responseCode = "400",
                    description = "The countryId was not found."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @DeleteMapping("/country/{countryId}")
    public void deleteCountry(@PathVariable long countryId) {
        logger.info("CONTROLLER -> Delete the country: " + countryId);
        countryService.deleteCountry(countryId);
    }

    @Operation(
            description = "Update a specific country.",
            method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The country was successfully updated."),
            @ApiResponse(responseCode = "400",
                    description = "The countryId was not found or the country schema is invalid."),
            @ApiResponse(responseCode = "409",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PatchMapping("/country/{countryId}")
    public void updateCountry(@RequestBody CountryDTO updatedCountry, @PathVariable long countryId) {
        logger.info("CONTROLLER -> Update the country: " + countryId);
        countryService.updateCountry(countryId, updatedCountry);
    }
}
