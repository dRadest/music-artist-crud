package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "length")
    private int length; // in seconds
    @OneToMany(mappedBy = "song")
    private Set<AlbumSong> albumSongs;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Set<AlbumSong> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(Set<AlbumSong> albumSongs) {
        this.albumSongs = albumSongs;
    }
}
