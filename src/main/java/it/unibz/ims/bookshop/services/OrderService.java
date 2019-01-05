package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Order;
import it.unibz.ims.bookshop.models.Orderline;
import it.unibz.ims.bookshop.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderlineService orderlineService;

    public Optional<Order> createOrUpdate(Order order) {
        return Optional.ofNullable( orderRepository.save(order) );
    }

    public Optional<Order> find(UUID orderId) {
        Optional<Order> order =  orderRepository.findById(orderId);

        if (!order.isPresent()) {
            return order;
        }

        order.get().setOrderlines( orderlineService.findByOrderId(orderId) );
        return order;
    }

    public Optional<List<Order>> findAllByCustomerId(String customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    public void delete(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    public Optional<Order> createOrderline(Orderline orderlineToCreate) {
        UUID orderId = orderlineToCreate.getOrderId();
        Optional<Orderline> createdOrderline = this.orderlineService.update(orderlineToCreate);

        if (!createdOrderline.isPresent()) {
            return this.find(orderId);
        }

        return this.find(createdOrderline.get().getOrderId());
    }

    public Optional<Order> updateOrderline(UUID orderlineId, Orderline orderlineUpdates) {
        Optional<Orderline> updatedOrderline = this.orderlineService.update(orderlineId, orderlineUpdates);

        if (!updatedOrderline.isPresent()) {
            return Optional.ofNullable(null);
        }

        return this.find(updatedOrderline.get().getOrderId());
    }

    public void deleteOrderLine(UUID orderlineId) {
        orderlineService.delete(orderlineId);
    }

    public void deleteAllOrderLinesInBatch(List<Orderline> orderlines) {
        orderlineService.deleteInBatch(orderlines);
    }
}
