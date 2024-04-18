package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BandJpaRepository extends JpaRepository<Band, Long> {
    @Query("select b from band b " +
            "where lower(b.name) like lower(concat('%', :searchTerm, '%'))")
    List<Band> search(@Param("searchTerm") String searchTerm);
}
