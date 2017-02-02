package fr.sri.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by srahmi on 01/02/2017.
 */
@Entity
public class ProgrammingLanguage {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    String name;

    public ProgrammingLanguage() {
    }

    public ProgrammingLanguage(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
