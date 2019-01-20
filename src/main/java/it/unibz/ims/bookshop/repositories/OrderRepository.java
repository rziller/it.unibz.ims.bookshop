package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<List<Order>> findAllByCustomerId(String customerId);
}
