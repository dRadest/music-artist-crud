package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.AlbumSong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumSongJpaRepository extends JpaRepository<AlbumSong, Long> {
}
