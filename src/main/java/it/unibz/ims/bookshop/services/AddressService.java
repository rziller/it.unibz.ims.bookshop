package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Address;
import it.unibz.ims.bookshop.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;


@Service
public class AddressService {

    @Autowired
    public AddressRepository addressRepository;

    public Optional<Address> createOrUpdate(Address address) {
        return Optional.ofNullable( addressRepository.save(address) );
    }

    public Optional<List<Address>> createOrUpdateAll(List<Address> addresses) {
        return Optional.ofNullable( addressRepository.saveAll(addresses) );
    }

    public Optional<Address> findById(UUID addressId) {
        return addressRepository.findById(addressId);
    }

    public Address deserializeBillingAddress(MultiValueMap<String, String> addressSerialized) {
        return this.deserializeAddress("billing", addressSerialized);
    }

    public Address deserializeShippingAddress(MultiValueMap<String, String> addressSerialized) {
        return this.deserializeAddress("shipping", addressSerialized);
    }

    private Address deserializeAddress(String addressType, MultiValueMap<String, String> addressSerialized) {
        Map<String, String> addressSerializedMap = addressSerialized.toSingleValueMap();
        Address serializedAddress = new Address();

        String addressIdString = addressSerializedMap.getOrDefault(addressType + ".addressId", null);
        UUID addressId = (addressIdString != null) ? UUID.fromString(addressIdString) : UUID.randomUUID();

        serializedAddress.setAddressId(addressId);
        serializedAddress.setName( addressSerializedMap.getOrDefault(addressType + ".name", null) );
        serializedAddress.setSurname( addressSerializedMap.getOrDefault(addressType + ".surname", null) );
        serializedAddress.setStreet( addressSerializedMap.getOrDefault(addressType + ".street", null) );
        serializedAddress.setZipCode( addressSerializedMap.getOrDefault(addressType + ".zipCode", null) );
        serializedAddress.setCity( addressSerializedMap.getOrDefault(addressType + ".city", null) );
        serializedAddress.setCountryId( Integer.valueOf( addressSerializedMap.getOrDefault(addressType + ".countryId", null) ));

        return serializedAddress;
    }


}
