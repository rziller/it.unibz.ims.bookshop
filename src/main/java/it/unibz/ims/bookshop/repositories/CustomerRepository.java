package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
