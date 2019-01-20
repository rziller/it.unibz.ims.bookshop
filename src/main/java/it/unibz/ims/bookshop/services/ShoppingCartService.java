package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Order;
import it.unibz.ims.bookshop.models.Orderline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingCartService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    public Optional<Order> findBySession(HttpSession session) {
        Optional<UUID> shoppingCartId = Optional.ofNullable( sessionService.getShoppingCartId(session) );
        Optional<Order> shoppingCart;

        if ( !shoppingCartId.isPresent() ) {
            shoppingCart = this.create();

            if ( shoppingCart.isPresent() ) {
                sessionService.setShoppingCartId(session, shoppingCart.get().getOrderId());
                sessionService.setShoppingCartTotalToZero(session);
            }
        } else {
            shoppingCart = orderService.find( shoppingCartId.get() );
        }

        return shoppingCart;
    }

    public void deleteById(UUID shoppingCartId) {
        orderService.delete(shoppingCartId);
    }

    public Optional<Order> addProduct(HttpSession session, MultiValueMap requestBody) {
        Optional<Order> shoppingCart = this.findBySession(session);
        UUID shoppingCartId = shoppingCart.get().getOrderId();

        Optional<String> productIdString = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "productId");
        Optional<String> qtyString = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "qty");

        if ( !productIdString.isPresent() ) {
            return shoppingCart;
        }

        UUID productId = UUID.fromString(productIdString.get());
        int qty = ( qtyString.isPresent() ) ? Integer.valueOf( qtyString.get() ) : 0;

        for (Orderline orderline: shoppingCart.get().getOrderlines()) {
            if (orderline.getProductId().equals(productId)) {
                orderline.setQty( orderline.getQty() + qty );
                return orderService.updateOrderline(orderline.getOrderlineId(), orderline);
            }
        }

        Orderline product = new Orderline();
        product.setOrderId(shoppingCartId);
        product.setProductId(productId);
        product.setQty(qty);

        Optional<Order> orderUpdated = orderService.createOrderline(product);

        if ( !orderUpdated.isPresent() )
        {
            return Optional.ofNullable(null);
        }

        sessionService.incrementShoppingCartTotalByOne(session);

        return orderUpdated;
    }

    public Optional<Order> updateProduct(HttpSession session, MultiValueMap requestBody) {
        Optional<String> orderlineIdString = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "orderlineId");
        Optional<String> qtyString = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "qty");
        int qty = Integer.valueOf(qtyString.get());

        if ( !orderlineIdString.isPresent() ) {
            return this.findBySession(session);
        }

        if ( qty <= 0 ) {
            return this.deleteProduct(session, requestBody);
        }

        Orderline orderlineUpdates = new Orderline();
        orderlineUpdates.setQty(qty);

        orderService.updateOrderline(UUID.fromString(orderlineIdString.get()), orderlineUpdates);
        return this.findBySession(session);
    }

    public Optional<Order> deleteProduct(HttpSession session, MultiValueMap requestBody) {
        Optional<String> orderlineIdString = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "orderlineId");

        if ( !orderlineIdString.isPresent() ) {
            return this.findBySession(session);
        }

        orderService.deleteOrderLine( UUID.fromString( orderlineIdString.get() ) );
        sessionService.decrementShoppingCartTotalByOne(session);

        return this.findBySession(session);
    }

    public Optional<Order> deleteAllProducts(HttpSession session, MultiValueMap requestBody) {
        Optional<String> currentShoppingCartIdStringOptional = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "orderId");

        Optional<Order> currentShoppingCartOptional;
        UUID currentShoppingCartId;

        if ( !currentShoppingCartIdStringOptional.isPresent() )
        {
            currentShoppingCartOptional = this.findBySession(session);
            currentShoppingCartId = currentShoppingCartOptional.get().getOrderId();
        }
        else {
            currentShoppingCartId = UUID.fromString( currentShoppingCartIdStringOptional.get() );
            currentShoppingCartOptional = orderService.find(currentShoppingCartId);
        }

        if ( !currentShoppingCartOptional.isPresent() ) {
            return Optional.ofNullable(null);
        }

        orderService.deleteAllOrderLinesInBatch(currentShoppingCartOptional.get().getOrderlines());
        sessionService.setShoppingCartTotalToZero(session);

        Optional<Order> shoppingCartUpdatedOptional = orderService.find(currentShoppingCartId);

        if ( !shoppingCartUpdatedOptional.isPresent() ) {
            Order currentShoppingCart = currentShoppingCartOptional.get();
            currentShoppingCart.setOrderlines(null);
            return Optional.ofNullable(currentShoppingCart);
        }

        return shoppingCartUpdatedOptional;
    }

    private Optional<Order> create() {
        Order shoppingCart = new Order();
        return orderService.createOrUpdate(shoppingCart);
    }
}
