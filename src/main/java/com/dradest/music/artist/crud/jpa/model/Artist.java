package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;

@Entity(name = "artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    // ... other fields such as yearOfBirth ... //
    @ManyToOne(fetch = FetchType.EAGER) // FIXME: change to LAZY and load entity where needed in the UI
    @JoinColumn(name = "country_id")
    private Country country;
    @ManyToOne(fetch = FetchType.EAGER)
    private Band band; // bidirectional mapping

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}
