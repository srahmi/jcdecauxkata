package fr.sri.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by srahmi on 01/02/2017.
 */
@Entity
public class Developer {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    String name;

    @OneToOne(cascade = CascadeType.ALL)
    ProgrammingLanguage programmingLanguage;


    public Developer() { // jpa needs this
    }

    public Developer(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguages) {
        this.programmingLanguage = programmingLanguages;
    }
}
