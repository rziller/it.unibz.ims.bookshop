package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.models.Address;
import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PageableService pageableService;

    public Page<Product> findAll(Map<String, String> queryParameters, HttpServletRequest request) {
        Pageable pageable = pageableService.createPageRequest(queryParameters);
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(String productId) {
        return this.findById( UUID.fromString(productId) );
    }

    public Optional<Product> findById(UUID productId) {
        return productRepository.findById(productId);
    }

    public boolean deleteById(UUID productId) {
        productRepository.deleteById(productId);
        return true;
    }

    public Optional<Product> edit(MultiValueMap<String, String> productSerialized) {
        Product serializedProduct = this.deserializeProduct(productSerialized);

        if (serializedProduct == null) {
            return Optional.empty();
        }

        Product savedProduct = productRepository.save(serializedProduct);
        return Optional.of(savedProduct);
    }

    public Page<Product> findSearchResult(Map<String, String> queryParameters, HttpServletRequest request) {
        String SearchQuery = queryParameters.get("query");
        Pageable pageable = pageableService.createPageRequest(queryParameters);
        return productRepository.find(SearchQuery,pageable);
    }

    private Product deserializeProduct(MultiValueMap<String, String> productSerialized) {
        Map<String, String> productSerializedMap = productSerialized.toSingleValueMap();
        Product serializedProduct = new Product();

        String productIdString = productSerializedMap.getOrDefault("productId", null);
        UUID productId = (productIdString != null) ? UUID.fromString(productIdString) : UUID.randomUUID();

        serializedProduct.setProductId(productId);
        serializedProduct.setDescription( productSerializedMap.getOrDefault( "description", null) );
        serializedProduct.setName( productSerializedMap.getOrDefault("name", null) );
        serializedProduct.setPrice( Float.valueOf( productSerializedMap.getOrDefault("price", null) ) );

        return serializedProduct;
    }
}
