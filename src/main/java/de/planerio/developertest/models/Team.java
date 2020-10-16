package de.planerio.developertest.models;

import de.planerio.developertest.models.League;
import de.planerio.developertest.models.Player;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private League league;

    @OneToMany(targetEntity = Player.class)
    @Size(max=25)
    private List<Player> players;

    public Team(){ }

    public long getId() {
        return id;
    }

    public Team setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public League getLeague() {
        return league;
    }

    public Team setLeague(League league) {
        this.league = league;
        return this;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Team setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }
}
