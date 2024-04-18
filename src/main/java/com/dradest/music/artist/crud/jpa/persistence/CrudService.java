package com.dradest.music.artist.crud.jpa.persistence;

import com.dradest.music.artist.crud.jpa.model.Band;
import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.repositories.BandJpaRepository;
import com.dradest.music.artist.crud.jpa.repositories.CountryJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudService {
    private final CountryJpaRepository countryJpaRepository;
    private final BandJpaRepository bandJpaRepository;

    public CrudService(CountryJpaRepository countryJpaRepository, BandJpaRepository bandJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;
        this.bandJpaRepository = bandJpaRepository;
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


}
