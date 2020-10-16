package de.planerio.developertest.services.dtos;

import java.util.List;

public class TeamDTO {

    private long id;

    private String name;

    private LeagueDTO leagueDTO;

    private Iterable<PlayerDTO> playersDTO;

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

    public LeagueDTO getLeague() {
        return leagueDTO;
    }

    public void setLeague(LeagueDTO leagueDTO) {
        this.leagueDTO = leagueDTO;
    }

    public Iterable<PlayerDTO> getPlayers() {
        return playersDTO;
    }

    public void setPlayers(Iterable<PlayerDTO> playersDTO) {
        this.playersDTO = playersDTO;
    }
}
