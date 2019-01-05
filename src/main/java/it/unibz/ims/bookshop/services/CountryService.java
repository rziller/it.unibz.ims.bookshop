package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Country;
import it.unibz.ims.bookshop.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PageableService pageableService;

    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
