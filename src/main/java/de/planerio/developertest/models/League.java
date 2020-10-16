package de.planerio.developertest.models;

import de.planerio.developertest.exceptions.NameEmptyException;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(optional = false)
    private Country country;

    @OneToMany(targetEntity = Team.class)
    @Size(max=20)
    private List<Team> teams;

    public League(){ }

    public long getId() {
        return id;
    }

    public League setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public League setName(String name) throws NameEmptyException{
        if (name == null || name.equals(""))
            throw new NameEmptyException();
        this.name = name;
        return this;
    }

    public Country getCountry() {
        return country;
    }

    public League setCountry(Country country) {
        this.country = country;
        return this;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public League setTeams(List<Team> teams) {
        this.teams = teams;
        return this;
    }
}
