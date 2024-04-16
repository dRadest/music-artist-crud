package com.dradest.music.artist.crud.jpa;

import com.dradest.music.artist.crud.MusicArtistCrudApplication;
import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.repositories.CountryJpaRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MusicArtistCrudApplication.class)
public class CountryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryTest.class);

    @Autowired
    private CountryJpaRepository countryJpaRepository;

    @Test
    public void testCountryInsertion() {
        LOGGER.info("---- CountryTest.testCountryInsertion() ----");
        assertNotNull(countryJpaRepository);

        // automatically populated via data.sql
        long count = countryJpaRepository.count();
        assertEquals(3, count);

        Country slovenia = new Country();
        slovenia.setName("Slovenia");
        slovenia.setCode("SI");
        countryJpaRepository.save(slovenia);

        count = countryJpaRepository.count();
        assertEquals(4, count);
    }
}
