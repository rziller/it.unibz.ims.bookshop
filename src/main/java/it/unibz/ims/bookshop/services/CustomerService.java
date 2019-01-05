package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Customer;
import it.unibz.ims.bookshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository customerRepository;

    public void handleAuthenticatedCustomer(Authentication auth) {
        Optional<Customer> authCustomerOptional = this.getAuthenticatedCustomer(auth);

        if (!authCustomerOptional.isPresent())
        {
            return;
        }

        Customer authCustomer = authCustomerOptional.get();


        if ( customerRepository.existsById(authCustomer.getCustomerId()) )
        {
            return;
        }

        authCustomer.setRegistered(true);
        this.createOrUpdate(authCustomer);
    }

    public Optional<Customer> getAuthenticatedCustomer(Authentication auth) {
        if (auth == null || !auth.isAuthenticated())
        {
            return Optional.ofNullable(null);
        }

        HashMap<String, Object> authCustomerDetails = (HashMap<String, Object>) ((OAuth2Authentication) auth).getUserAuthentication().getDetails();
        return Optional.ofNullable( this.deserializeAuthenticatedCustomer(authCustomerDetails) );
    }

    public Optional<Customer> createOrUpdate(Customer customer) {
        return Optional.ofNullable( customerRepository.save(customer) );
    }

    public Customer deserializeCustomer(MultiValueMap<String, String> customerSerialized) {
        Customer deserializedCustomer = new Customer();
        deserializedCustomer.setName( customerSerialized.getFirst("customer.name") );
        deserializedCustomer.setSurname( customerSerialized.getFirst("customer.surname") );
        deserializedCustomer.setEmail( customerSerialized.getFirst("customer.email") );
        return deserializedCustomer;
    }

    public Customer deserializeAuthenticatedCustomer(HashMap<String, Object> customerSerialized) {
        Customer deserializedCustomer = new Customer();

        String sub = (String) customerSerialized.get("sub");
        String givenname = (String) customerSerialized.get("given_name");
        String familyname = (String) customerSerialized.get("family_name");
        String email = (String) customerSerialized.get("email");

        deserializedCustomer.setCustomerId(sub);
        deserializedCustomer.setName(givenname);
        deserializedCustomer.setSurname(familyname);
        deserializedCustomer.setEmail(email);
        deserializedCustomer.setRegistered(true);

        return deserializedCustomer;
    }
}
