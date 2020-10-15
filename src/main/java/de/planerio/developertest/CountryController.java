package de.planerio.developertest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
public class CountryController {

    Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryRepository countryRepo;

    @Operation(
            description = "List all available countries.",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "The list of countries will be provided."),
            @ApiResponse(responseCode = "500",
                         description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @GetMapping("/countries")
    public Page<Country> getCountries(@Param(value = "page") int page,
                                      @Param(value = "size") int size) {
        logger.info("List all countries");
        Pageable pageable = PageRequest.of(page, size);
        return countryRepo.findAll(pageable);
    }

    @Operation(
            description = "Create a new country.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The country was successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The country schema is invalid and therefore the country has not been created."),
            @ApiResponse(responseCode = "500",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/country")
    public Country createCountry(@RequestBody Country c) {
        logger.info("Add new country");
        return countryRepo.save(c);
    }

    @Operation(
            description = "Create a list of countries.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The countries were successfully created."),
            @ApiResponse(responseCode = "400",
                    description = "The country schema is invalid and therefore the country has not been created."),
            @ApiResponse(responseCode = "500",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PostMapping("/countries")
    public Iterable<Country> createCountries(@RequestBody Iterable<Country> c) {
        logger.info("Add a list of countries");
        return countryRepo.saveAll(c);
    }

    @Operation(
            description = "Remove a specific country.",
            method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The country was successfully removed."),
            @ApiResponse(responseCode = "400",
                    description = "The countryId was not found."),
            @ApiResponse(responseCode = "500",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @DeleteMapping("/country/{countryId}")
    public void deleteCountry(@PathVariable long countryId) {
        logger.info("Delete the country: " + countryId);
        countryRepo.deleteById(countryId);
    }

    @Operation(
            description = "Update a specific country.",
            method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The country was successfully updated."),
            @ApiResponse(responseCode = "400",
                    description = "The countryId was not found or the country schema is invalid."),
            @ApiResponse(responseCode = "500",
                    description = "An unexpected error has occurred. The error has been logged and is being investigated.") })
    @PutMapping("/country/{countryId}")
    public void updateCountry(@RequestBody Country updatedCountry, @PathVariable long countryId) {
        logger.info("Update the country: " + countryId);
        countryRepo.findById(countryId).orElseThrow();
        updatedCountry.setId(countryId);
        countryRepo.save(updatedCountry);
    }
}
