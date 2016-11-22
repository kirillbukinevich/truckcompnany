package com.truckcompany.service.dto;

import com.truckcompany.config.Constants;

import com.truckcompany.domain.Authority;
import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private String city;
    private String street;
    private String flat;
    private String house;
    private String passport;


    @Email
    @Size(min = 5, max = 100)
    private String email;

    private String logo;

    private Company company;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    private ZonedDateTime birthDate;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getLogo(), user.getActivated(), user.getLangKey(), user.getBirthDate(),
            user.getCity(), user.getStreet(), user.getHouse(), user.getFlat(), user.getPassport(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()), user.getCompany());
    }

    public UserDTO(Long id, String login, String firstName, String lastName,
                   String email, String logo, boolean activated, String langKey, ZonedDateTime birthDate,
                   String city, String street, String house, String flat, String passport, Set<String> authorities, Company company) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.logo = logo;
        this.activated = activated;
        this.langKey = langKey;
        this.birthDate = birthDate;
        this.city = city;
        this.street = street;
        this.flat = flat;
        this.house = house;
        this.passport = passport;
        this.authorities = authorities;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getLogo() {
        return logo;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getFlat() {
        return flat;
    }

    public String getHouse() {
        return house;
    }

    public String getPassport() {
        return passport;
    }

    public Company getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }


}
