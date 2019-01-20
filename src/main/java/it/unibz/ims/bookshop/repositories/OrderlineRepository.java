package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Orderline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface OrderlineRepository extends JpaRepository<Orderline, UUID> {
    List<Orderline> deleteByOrderId(UUID orderId);
    List<Orderline> findByOrderId(UUID orderId);
    Optional<Orderline> findByOrderIdAndProductId(UUID orderId, UUID productId);
}
