package de.planerio.developertest.models;

import de.planerio.developertest.enums.Position;
import de.planerio.developertest.exceptions.NameEmptyException;
import de.planerio.developertest.exceptions.ShirtNumberException;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Team team;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(nullable = false)
    @Min(1)
    @Max(99)
    private int shirtNumber;

    public Player(){ }

    public long getId() {
        return id;
    }

    public Player setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) throws NameEmptyException{
        if (name == null || name.equals(""))
            throw new NameEmptyException();
        this.name = name;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public Player setTeam(Team team) {
        this.team = team;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public Player setPosition(Position position) {
        this.position = position;
        return this;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public Player setShirtNumber(int shirtNumber) {
        if (shirtNumber > 99 || shirtNumber < 1) throw new ShirtNumberException();
        this.shirtNumber = shirtNumber;
        return this;
    }
}
