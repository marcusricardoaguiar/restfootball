package de.planerio.developertest.services.dtos;

public class PlayerDTO {

    private long id;

    private String name;

    private TeamDTO teamDTO;

    private String position;

    private int shirtNumber;

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

    public TeamDTO getTeam() {
        return teamDTO;
    }

    public void setTeam(TeamDTO teamDTO) {
        this.teamDTO = teamDTO;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }
}
