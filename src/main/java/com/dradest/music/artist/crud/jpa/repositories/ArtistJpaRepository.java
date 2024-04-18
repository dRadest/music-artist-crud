package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistJpaRepository extends JpaRepository<Artist, Long> {
    @Query("select a from artist a " +
            "where lower(a.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(a.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Artist> search(@Param("searchTerm") String searchTerm);
}
