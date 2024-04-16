package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_label")
    private String recordLabel;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @OneToMany(mappedBy = "album")
    private Set<AlbumSong> albumSongs;
    @ManyToOne(fetch = FetchType.EAGER)
    private Band band; // bidirectional mapping


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
