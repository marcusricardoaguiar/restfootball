package de.planerio.developertest.services.dtos;

public class LeagueDTO {

    private long id;

    private String name;

    private CountryDTO countryDTO;

    private Iterable<TeamDTO> teamsDTO;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryDTO getCountry() {
        return countryDTO;
    }

    public void setCountry(CountryDTO countryDTO) {
        this.countryDTO = countryDTO;
    }

    public Iterable<TeamDTO> getTeams() {
        return teamsDTO;
    }

    public void setTeams(Iterable<TeamDTO> teamsDTO) {
        this.teamsDTO = teamsDTO;
    }
}
