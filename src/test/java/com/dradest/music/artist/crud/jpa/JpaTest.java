package com.dradest.music.artist.crud.jpa;

import com.dradest.music.artist.crud.MusicArtistCrudApplication;
import com.dradest.music.artist.crud.jpa.model.*;
import com.dradest.music.artist.crud.jpa.repositories.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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
    @Autowired
    private AlbumJpaRepository albumJpaRepository;
    @Autowired
    private SongJpaRepository songJpaRepository;
    @Autowired
    private AlbumSongJpaRepository albumSongJpaRepository;

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

    @Test
    public void testAlbumSongInsertion() {
        List<Band> bands = bandJpaRepository.search("ac");
        Band acDCBand;
        if (bands.isEmpty()) {
            acDCBand = new Band();
            acDCBand.setName("AC/DC");
            acDCBand = bandJpaRepository.save(acDCBand);
        } else {
            acDCBand = bands.get(0);
        }

        Album highVoltageAlbum = new Album();
        highVoltageAlbum.setTitle("High Voltage");
        highVoltageAlbum.setBand(acDCBand);
        highVoltageAlbum.setRecordLabel("Albert Productions");
        highVoltageAlbum.setReleaseDate(LocalDate.of(1976, Month.MAY, 14));
        highVoltageAlbum = albumJpaRepository.save(highVoltageAlbum);

        Song longWayToTheTop = new Song();
        longWayToTheTop.setTitle("It's a Long Way to the Top (If You Wanna Rock 'n' Roll)");
        longWayToTheTop.setLength(312);
        longWayToTheTop = songJpaRepository.save(longWayToTheTop);

        Song rockNRollSinger = new Song();
        rockNRollSinger.setTitle("Rock 'n' Roll Singer");
        rockNRollSinger.setLength(304);
        rockNRollSinger = songJpaRepository.save(rockNRollSinger);

        AlbumSong albumSong1 = new AlbumSong();
        albumSong1.setAlbum(highVoltageAlbum);
        albumSong1.setSong(longWayToTheTop);
        albumSong1.setPosition(1);
        albumSong1 = albumSongJpaRepository.save(albumSong1);

        AlbumSong albumSong2 = new AlbumSong();
        albumSong2.setAlbum(highVoltageAlbum);
        albumSong2.setSong(rockNRollSinger);
        albumSong2.setPosition(2);
        albumSong2 = albumSongJpaRepository.save(albumSong2);

        Optional<Album> optAlbumById = albumJpaRepository.findById(highVoltageAlbum.getId());
        assertTrue(optAlbumById.isPresent());
        Album albumById = optAlbumById.get();
        assertNotNull(albumById);
        assertEquals(highVoltageAlbum.getTitle(), albumById.getTitle());
        assertEquals(highVoltageAlbum.getRecordLabel(), albumById.getRecordLabel());
    }
}
