package de.planerio.developertest.models;

import de.planerio.developertest.enums.Language;
import de.planerio.developertest.exceptions.NameEmptyException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.*;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language;

    public Country(){ }

    public long getId() {
        return id;
    }

    public Country setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Country setName(String name) throws NameEmptyException{
        if (name == null || name.equals(""))
            throw new NameEmptyException();
        this.name = name;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public Country setLanguage(Language language) {
        this.language = language;
        return this;
    }
}
