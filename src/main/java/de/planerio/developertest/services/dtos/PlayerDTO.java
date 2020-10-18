package de.planerio.developertest.services.dtos;

public class PlayerDTO {

    private long id;

    private String name;

    private String firstName;

    private String lastName;

    private TeamDTO teamDTO;

    private String position;

    private int shirtNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() { return this.name; }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

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
