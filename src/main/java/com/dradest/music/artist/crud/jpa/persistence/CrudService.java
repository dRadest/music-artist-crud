package com.dradest.music.artist.crud.jpa.persistence;

import com.dradest.music.artist.crud.jpa.model.*;
import com.dradest.music.artist.crud.jpa.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrudService {
    private final CountryJpaRepository countryJpaRepository;
    private final BandJpaRepository bandJpaRepository;
    private final ArtistJpaRepository artistJpaRepository;
    private final AlbumJpaRepository albumJpaRepository;
    private final SongJpaRepository songJpaRepository;
    private final AlbumSongJpaRepository albumSongJpaRepository;

    public CrudService(CountryJpaRepository countryJpaRepository, BandJpaRepository bandJpaRepository, ArtistJpaRepository artistJpaRepository, AlbumJpaRepository albumJpaRepository, SongJpaRepository songJpaRepository, AlbumSongJpaRepository albumSongJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;
        this.bandJpaRepository = bandJpaRepository;
        this.artistJpaRepository = artistJpaRepository;
        this.albumJpaRepository = albumJpaRepository;
        this.songJpaRepository = songJpaRepository;
        this.albumSongJpaRepository = albumSongJpaRepository;
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
        artistJpaRepository.removeBandFromArtists(band);
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

    public Album findAlbumById(Long id) {
        Optional<Album> optAlbum = albumJpaRepository.findById(id);
        return optAlbum.orElse(null);
    }

    public void deleteAlbum(Album album) {
        //albumJpaRepository.removeBandFromAlbums(album.getBand());
        // update band
        Band band = album.getBand();
        band.getAlbums().remove(album);
        bandJpaRepository.save(band);
        albumJpaRepository.delete(album);
    }

    public void saveAlbum(Album album) {
        if (album == null) {
            System.err.println("Album is null. Are you sure you have connected your form to the application?");
            return;
        }
        Album persistedAlbum = albumJpaRepository.save(album);
        album.getAlbumSongs().forEach(albumSong -> {
            Song persistedSong = songJpaRepository.save(albumSong.getSong());
            albumSong.setAlbum(persistedAlbum);
            albumSong.setSong(persistedSong);
            albumSongJpaRepository.save(albumSong);
        });
    }
}
