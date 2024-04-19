package com.dradest.music.artist.crud.jpa.persistence;

import com.dradest.music.artist.crud.jpa.model.Album;
import com.dradest.music.artist.crud.jpa.model.Artist;
import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.repositories.AlbumJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.ArtistJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.BandJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.CountryJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudService {
    private final CountryJpaRepository countryJpaRepository;
    private final BandJpaRepository bandJpaRepository;
    private final ArtistJpaRepository artistJpaRepository;
    private final AlbumJpaRepository albumJpaRepository;

    public CrudService(CountryJpaRepository countryJpaRepository, BandJpaRepository bandJpaRepository, ArtistJpaRepository artistJpaRepository, AlbumJpaRepository albumJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;
        this.bandJpaRepository = bandJpaRepository;
        this.artistJpaRepository = artistJpaRepository;
        this.albumJpaRepository = albumJpaRepository;
    }

    public List<Country> findAllCountries(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return countryJpaRepository.findAll();
        } else {
            return countryJpaRepository.search(stringFilter);
        }
    }

    public long countCountries() {
        return countryJpaRepository.count();
    }

    public void deleteCountry(Country country) {
        countryJpaRepository.delete(country);
    }

    public void saveCountry(Country country) {
        if (country == null) {
            System.err.println("Country is null. Are you sure you have connected your form to the application?");
            return;
        }
        countryJpaRepository.save(country);
    }

    public List<Band> findAllBands(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return bandJpaRepository.findAll();
        } else {
            return bandJpaRepository.search(stringFilter);
        }
    }

    public void saveBand(Band band) {
        if (band == null) {
            System.err.println("Band is null. Are you sure you have connected your form to the application?");
            return;
        }
        bandJpaRepository.save(band);
    }

    public void deleteBand(Band band) {
        bandJpaRepository.delete(band);
    }

    public List<Artist> findAllArtists(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return artistJpaRepository.findAll();
        } else {
            return artistJpaRepository.search(stringFilter);
        }
    }

    public void deleteArtist(Artist artist) {
        artistJpaRepository.delete(artist);
    }

    public void saveArtist(Artist artist) {
        if (artist == null) {
            System.err.println("Artist is null. Are you sure you have connected your form to the application?");
            return;
        }
        artistJpaRepository.save(artist);
    }

    public List<Album> findAllAlbums(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return albumJpaRepository.findAll();
        } else {
            return albumJpaRepository.search(stringFilter);
        }
    }

    public void deleteAlbum(Album album) {
        albumJpaRepository.delete(album);
    }

    public void saveAlbum(Album album) {
        if (album == null) {
            System.err.println("Album is null. Are you sure you have connected your form to the application?");
            return;
        }
        albumJpaRepository.save(album);
    }
}
