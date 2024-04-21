package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "band")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @OneToMany(
            mappedBy = "band",
            // cascade = CascadeType.REMOVE,
            // orphanRemoval = true,
            fetch = FetchType.EAGER // FIXME: set fetch to LAZY and load entities where needed
    )
    // technically, an artist could belong to more than one band, but we won't cover that case (it would require a ManyToMany mapping)
    private Set<Artist> members = new HashSet<>();
    @OneToMany(
            mappedBy = "band",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Album> albums = new HashSet<>();


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

    public Set<Artist> getMembers() {
        return members;
    }

    public void setMembers(Set<Artist> members) {
        this.members = members;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
