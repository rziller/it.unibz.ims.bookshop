package it.unibz.ims.bookshop.repositories;

import it.unibz.ims.bookshop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where lower(p.name) like lower(concat('%',?1,'%')) or lower(p.description) like lower(concat('%',?1,'%'))")
    public Page<Product> find(String query, Pageable pageable);

}
