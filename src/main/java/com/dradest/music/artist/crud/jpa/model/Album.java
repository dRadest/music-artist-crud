package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "record_label")
    private String recordLabel;
    @NotNull
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;
    // FIXME: set fetch to LAZY and load entities where needed
    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<AlbumSong> albumSongs = new HashSet<>();
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Band band; // bidirectional mapping

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<AlbumSong> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(Set<AlbumSong> albumSongs) {
        this.albumSongs = albumSongs;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}
