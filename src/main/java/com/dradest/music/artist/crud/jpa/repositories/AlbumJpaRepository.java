package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumJpaRepository extends JpaRepository<Album, Long> {
    @Query("select a from album a " +
            "where lower(a.title) like lower(concat('%', :searchTerm, '%'))")
    List<Album> search(@Param("searchTerm") String searchTerm);
}
