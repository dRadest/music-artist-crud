package com.dradest.music.artist.crud.jpa;

import com.dradest.music.artist.crud.MusicArtistCrudApplication;
import com.dradest.music.artist.crud.jpa.model.Artist;
import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.repositories.ArtistJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.BandJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.CountryJpaRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MusicArtistCrudApplication.class)
public class JpaTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaTest.class);

    @Autowired
    private CountryJpaRepository countryJpaRepository;
    @Autowired
    private ArtistJpaRepository artistJpaRepository;
    @Autowired
    private BandJpaRepository bandJpaRepository;

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

    @Test
    public void testBandInsertion() {
        Country greatBritain;
        Optional<Country> optCountry = countryJpaRepository.findByCode("GB");
        if (optCountry.isPresent()) {
            greatBritain = optCountry.get();
        } else {
            greatBritain = new Country();
            greatBritain.setCode("GB");
            greatBritain.setName("Great Britain");
            greatBritain = countryJpaRepository.save(greatBritain);
        }

        Band ledZeppelin = new Band();
        ledZeppelin.setName("Led Zeppelin");
        ledZeppelin = bandJpaRepository.save(ledZeppelin);

        Artist jimmyPage = new Artist();
        jimmyPage.setFirstName("Jimmy");
        jimmyPage.setLastName("Page");
        jimmyPage.setCountry(greatBritain);
        jimmyPage.setBand(ledZeppelin);
        jimmyPage = artistJpaRepository.save(jimmyPage);

        Optional<Band> optBandById = bandJpaRepository.findById(ledZeppelin.getId());
        assertTrue(optBandById.isPresent());
        Band bandById = optBandById.get();
        assertEquals(ledZeppelin.getName(), bandById.getName());

        assertNotNull(bandById.getMembers());
        assertEquals(1, bandById.getMembers().size());
        Artist member = bandById.getMembers().iterator().next();
        assertEquals(jimmyPage.getFirstName(), member.getFirstName());
        assertEquals(jimmyPage.getLastName(), member.getLastName());
    }
}
