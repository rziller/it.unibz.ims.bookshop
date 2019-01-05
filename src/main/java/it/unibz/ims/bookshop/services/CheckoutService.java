package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Address;
import it.unibz.ims.bookshop.models.CheckoutResult;
import it.unibz.ims.bookshop.models.Customer;
import it.unibz.ims.bookshop.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    SessionService sessionService;

    public CheckoutResult checkout(Optional<Customer> customerOptional, Optional<Order> shoppingCart,
                                   HttpSession session, MultiValueMap<String, String> requestBody) {
        CheckoutResult checkoutResult = new CheckoutResult();

        if (!shoppingCart.isPresent())
        {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        Order orderToPersistAndFreeze = shoppingCart.get();

        if ( orderToPersistAndFreeze.getTotalOrderlines() <= 0)
        {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        Address shippingAddress = addressService.deserializeShippingAddress(requestBody);
        Address billingAddress = addressService.deserializeBillingAddress(requestBody);

        if ( !shippingAddress.isValid() || !billingAddress.isValid() ) {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        Optional<List<Address>> persistedAddresses = this.persistAddresses(shippingAddress, billingAddress);

        if ( !persistedAddresses.isPresent() )
        {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        Integer paymentMethodId = this.getPaymentMethod(requestBody);

        if ( !customerOptional.isPresent() )
        {
            Customer anonymousCustomer = customerService.deserializeCustomer(requestBody);
            customerOptional = this.persistAnonymousCustomer(anonymousCustomer);

            if ( !customerOptional.isPresent() )
            {
                checkoutResult.setSuccess(false);
                return checkoutResult;
            }
        }
        else
        {
            customerOptional = this.persistAuthCustomer(customerOptional.get(), persistedAddresses.get(), paymentMethodId);

            if ( !customerOptional.isPresent() )
            {
               checkoutResult.setSuccess(false);
               return checkoutResult;
            }
        }

        Customer customer = customerOptional.get();

        if ( !customer.isValid() ) {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        Optional<Order> orderPersistedAndFrozen = this.persistAndFreezeOrder(orderToPersistAndFreeze, customer,
                persistedAddresses.get(), paymentMethodId);

        if ( !orderPersistedAndFrozen.isPresent() ) {
            checkoutResult.setSuccess(false);
            return checkoutResult;
        }

        sessionService.setShoppingCartTotalToZero(session);
        sessionService.removeShoppingCartId(session);
        checkoutResult.setSuccess(true);
        checkoutResult.setOrderId(orderPersistedAndFrozen.get().getOrderId());
        return checkoutResult;
    }

    private Optional<List<Address>> persistAddresses(Address shippingAddress, Address billingAddress) {
        ArrayList<Address> addressesToPersist = new ArrayList();
        addressesToPersist.add(shippingAddress);
        addressesToPersist.add(billingAddress);
        return addressService.createOrUpdateAll(addressesToPersist);
    }

    private Optional<Customer> persistAnonymousCustomer(Customer anonymousCustomer) {
        anonymousCustomer.setCustomerId( UUID.randomUUID().toString() );
        anonymousCustomer.setRegistered(false);
        return customerService.createOrUpdate(anonymousCustomer);
    }

    private Optional<Customer> persistAuthCustomer(Customer authCustomer,
                                                   List<Address> persistedAddresses,
                                                   Integer paymentMethodId) {
        authCustomer.setShippingAddressId( persistedAddresses.get(0).getAddressId() );
        authCustomer.setBillingAddressId( persistedAddresses.get(1).getAddressId() );
        authCustomer.setPaymentMethodId(paymentMethodId);
        authCustomer.setRegistered(true);
        return customerService.createOrUpdate(authCustomer);
    }

    private Optional<Order> persistAndFreezeOrder(Order orderToPersistAndFreeze,
                                                  Customer customer,
                                                  List<Address> persistedAddresses,
                                                  Integer paymentMethod) {
        orderToPersistAndFreeze.setCustomerId( customer.getCustomerId() );
        orderToPersistAndFreeze.setShipingAddressId( persistedAddresses.get(0).getAddressId() );
        orderToPersistAndFreeze.setBillingAddressId( persistedAddresses.get(1).getAddressId() );
        orderToPersistAndFreeze.setPaymentMethodId(paymentMethod);
        orderToPersistAndFreeze.freeze();
        return orderService.createOrUpdate(orderToPersistAndFreeze);
    }

    private Integer getPaymentMethod(MultiValueMap<String, String> requestBody) {
        return Integer.valueOf( requestBody.getFirst("payment") );
    }
}
