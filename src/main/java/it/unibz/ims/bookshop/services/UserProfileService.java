package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Address;
import it.unibz.ims.bookshop.models.Customer;
import it.unibz.ims.bookshop.models.Order;
import it.unibz.ims.bookshop.models.Orderline;
import it.unibz.ims.bookshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserProfileService {

    @Autowired
    public AddressService addressService;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public CustomerService customerService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public OrderlineService orderlineService;

    public Optional<Customer> findUserProfileMinimal(Authentication auth) {
        Optional<Customer> customerAuthOptional = customerService.getAuthenticatedCustomer(auth);

        if (!customerAuthOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        Customer customerAuth = customerAuthOptional.get();

        return customerRepository.findById(customerAuth.getCustomerId());

    }

    public Optional<Customer> findUserProfile(Authentication auth){
        Optional<Customer> customerPersistedOptional = this.findUserProfileMinimal(auth);

        if (!customerPersistedOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        Customer customerPersisted = customerPersistedOptional.get();
        customerPersisted.setShippingAddress( this.getShippingAddress(customerPersisted) );
        customerPersisted.setBillingAddress( this.getBillingAddress(customerPersisted) );

        return Optional.ofNullable(customerPersisted);
    }

    public Optional<Customer> updateUserProfile(Authentication auth, MultiValueMap<String, String> requestBody) {
        Optional<Customer> customerPersistedOptional = this.findUserProfileMinimal(auth);

        if (!customerPersistedOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        Customer customerToPersist = customerPersistedOptional.get();
        Address shippingAddressToPersist = addressService.deserializeShippingAddress(requestBody);
        Address billingAddressToPersist = addressService.deserializeBillingAddress(requestBody);
        Optional<Address> shippingAddressPersited = addressService.createOrUpdate(shippingAddressToPersist);

        if (!shippingAddressPersited.isPresent()) {
            return Optional.ofNullable(null);
        }

        Optional<Address> billingAddressPersited = addressService.createOrUpdate(billingAddressToPersist);

        if (!billingAddressPersited.isPresent()) {
            return Optional.ofNullable(null);
        }

        Customer customerUpdated = customerService.deserializeCustomer(requestBody);

        customerToPersist.setShippingAddressId(shippingAddressPersited.get().getAddressId());
        customerToPersist.setBillingAddressId(billingAddressPersited.get().getAddressId());
        customerToPersist.setEmail(customerUpdated.getEmail());

        Optional<Customer> customerPersisted = customerService.createOrUpdate(customerToPersist);

        if (!customerPersisted.isPresent()) {
            return Optional.ofNullable(null);
        }

        customerPersisted.get().setShippingAddress(shippingAddressPersited.get());
        customerPersisted.get().setBillingAddress(billingAddressPersited.get());

        return customerPersisted;
    }

    public Optional<List<Order>> findUserOrders(Authentication auth) {
        Optional<Customer> customerAuthOptional = this.findUserProfileMinimal(auth);

        if (!customerAuthOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        Optional<List<Order>> customerAuthOrdersOptional = orderService.findAllByCustomerId( customerAuthOptional.get().getCustomerId() );

        if (!customerAuthOrdersOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        List<Order> customerAuthOrders = customerAuthOrdersOptional.get();

        for(Order customerAuthOrder : customerAuthOrders) {
            List<Orderline> orderlines = orderlineService.findByOrderIdSimple(customerAuthOrder.getOrderId());
            customerAuthOrder.setOrderlines(orderlines);
        }

        return Optional.ofNullable(customerAuthOrders);
    }

    private Address getShippingAddress(Customer customerPersisted) {
        Address shippingAddress;
        UUID shippingAddressId = customerPersisted.getShippingAddressId();

        if (shippingAddressId == null) {
            shippingAddress = null;
        } else {
            Optional<Address> shippingAddressOptional = addressService.findById(shippingAddressId);

            if (!shippingAddressOptional.isPresent()) {
                shippingAddress = null;
            } else {
                shippingAddress = shippingAddressOptional.get();
            }
        }

        return shippingAddress;
    }

    private Address getBillingAddress(Customer customerPersisted) {
        Address billingAddress;
        UUID billingAddressId = customerPersisted.getBillingAddressId();

        if (billingAddressId == null) {
            billingAddress = null;
        } else {
            Optional<Address> billingAddressOptional = addressService.findById(billingAddressId);

            if (!billingAddressOptional.isPresent()) {
                billingAddress = null;
            } else {
                billingAddress = billingAddressOptional.get();
            }
        }

        return billingAddress;
    }

}
