package br.com.jrafael.catalog.repository.product;

import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.infrastructure.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends AbstractRepository<Product, String> {
}
