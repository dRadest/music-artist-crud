package com.dradest.music.artist.crud.jpa.repositories;

import com.dradest.music.artist.crud.jpa.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryJpaRepository extends JpaRepository<Country, Long> {
}
