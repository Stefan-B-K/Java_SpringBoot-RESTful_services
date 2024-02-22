package com.istef.rest_webservices.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//@JsonIgnoreProperties("middleName")
@JsonFilter("NoMiddleName")
public class Name {
    @NotNull
    @Size(min = 2, message = "Fisrt name should be at least 2 letters long")
    private String firstName;

//    @JsonIgnore
    private String middleName;

    @NotNull
    @Size(min = 4, message = "Last name should be at least 4 letters long")
    private String lastName;

    public Name(String firstName, String middleName, String lastName) {
        this(firstName, lastName);
        this.middleName = middleName;
    }

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.middleName = null;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
