package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongJpaRepository extends JpaRepository<Song, Long> {
}
