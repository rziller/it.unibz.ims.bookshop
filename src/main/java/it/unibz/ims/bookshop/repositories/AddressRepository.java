package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
