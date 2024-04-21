package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Artist;
import com.dradest.music.artist.crud.jpa.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArtistJpaRepository extends JpaRepository<Artist, Long> {
    @Query("select a from artist a " +
            "where lower(a.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(a.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Artist> search(@Param("searchTerm") String searchTerm);

    @Modifying
    @Transactional
    @Query("update artist set band = null where band = :band")
    int removeBandFromArtists(@Param("band")Band band);
}
