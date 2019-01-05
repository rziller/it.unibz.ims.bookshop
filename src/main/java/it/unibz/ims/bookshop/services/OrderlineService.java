package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Orderline;
import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.repositories.OrderlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderlineService {

    @Autowired
    private OrderlineRepository orderlineRepository;

    @Autowired
    private ProductService productService;

    public OrderlineService() {}

    public List<Orderline> findByOrderIdSimple(UUID orderId) {
        return orderlineRepository.findByOrderId(orderId);
    }

    public List<Orderline> findByOrderId(UUID orderId) {
        List<Orderline> orderlines = this.findByOrderIdSimple(orderId);

        for (Orderline orderline : orderlines) {
            UUID productId = orderline.getProductId();
            Optional<Product> associatedProduct = productService.findById(productId);

            if ( !associatedProduct.isPresent() )
            {
                orderline.setAssociatedProduct(null);
            }
            else
            {
                orderline.setAssociatedProduct( associatedProduct.get() );
            }
        }

        return orderlines;
    }

    public Optional<Orderline> create(Orderline orderlineToCreate) {
        if ( orderlineToCreate.getOrderId().equals(null) )
        {
            return null;
        }



        return Optional.ofNullable( orderlineRepository.save(orderlineToCreate) );
    }

    public Optional<Orderline> update(Orderline orderlineUpdates) {
        return this.create(orderlineUpdates);
    }

    public Optional<Orderline> update(UUID orderlineId, Orderline orderlineUpdates) {
        Optional<Orderline> existingOrderline = orderlineRepository.findById(orderlineId);

        if ( !existingOrderline.isPresent() ) {
            return this.create(orderlineUpdates);
        }

        Orderline orderlineToPersist = existingOrderline.get();
        orderlineToPersist.update(orderlineUpdates);
        return this.create(orderlineToPersist);
    }

    public void delete(UUID orderlineId) {
        orderlineRepository.deleteById(orderlineId);
    }

    public void deleteByOrderId(UUID orderId) {
        orderlineRepository.deleteByOrderId(orderId);
    }

    public void deleteInBatch(List<Orderline> orderlines) {
        orderlineRepository.deleteInBatch(orderlines);
    }
}
