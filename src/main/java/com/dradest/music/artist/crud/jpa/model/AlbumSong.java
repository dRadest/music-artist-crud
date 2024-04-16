package com.dradest.music.artist.crud.jpa.model;

import jakarta.persistence.*;

/**
 * Entity to implement ManyToMany relationship between Album and Song
 * It holds the information about song position on the album
 */
@Entity(name = "album_song")
public class AlbumSong {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    @Column(name = "position")
    private int position; // track position on the album

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
