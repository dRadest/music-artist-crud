package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<Country, Long> {
    @Query("select c from country c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%'))")
    List<Country> search(@Param("searchTerm") String searchTerm);

    Optional<Country> findByCode(String code);
}
