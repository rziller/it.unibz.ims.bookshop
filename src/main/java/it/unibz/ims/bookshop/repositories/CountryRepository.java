package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
