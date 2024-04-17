package com.dradest.music.artist.crud.jpa.persistence;

import com.dradest.music.artist.crud.jpa.model.Country;
import com.dradest.music.artist.crud.jpa.repositories.CountryJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudService {
    private final CountryJpaRepository countryJpaRepository;

    public CrudService(CountryJpaRepository countryJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;
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

    public void deleteContact(Country country) {
        countryJpaRepository.delete(country);
    }

    public void saveContact(Country country) {
        if (country == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        countryJpaRepository.save(country);
    }
}
