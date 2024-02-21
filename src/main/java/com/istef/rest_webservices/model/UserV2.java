package com.istef.rest_webservices.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;


import java.time.LocalDate;

public class UserV2 {
    private int id;

    @NotNull
    private Name name;

    @NotNull
    @Past(message = "Birthdate cannot be in the future")
    private LocalDate birthDate;


    public UserV2(int id, Name name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "UserV2{" +
                "id=" + id +
                ", name=" + name +
                ", birthDate=" + birthDate +
                '}';
    }
}
