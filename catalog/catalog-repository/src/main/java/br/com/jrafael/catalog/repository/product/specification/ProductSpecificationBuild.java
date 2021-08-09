package br.com.jrafael.catalog.repository.product.specification;

import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.infrastructure.repository.specification.builder.GenericSpecificationsBuilder;
import br.com.jrafael.infrastructure.repository.specification.factory.SpecificationFactory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductSpecificationBuild {

    private SpecificationFactory productSpecificationFactory;

    public ProductSpecificationBuild(SpecificationFactory productSpecificationFactory){
        this.productSpecificationFactory = productSpecificationFactory;
    }

    public Specification<Product> build(String nameOrDescription, BigDecimal maxPrice, BigDecimal minPrice) {
        GenericSpecificationsBuilder<Product> builder = new GenericSpecificationsBuilder<>();
        if (Objects.nonNull(nameOrDescription)) {
            builder.with(productSpecificationFactory.isLike("description", nameOrDescription).or(productSpecificationFactory.isLike("name", nameOrDescription)));
        }
        if (Objects.nonNull(minPrice)) {
            builder.with(productSpecificationFactory.isGreaterThan("price", minPrice));
        }

        if (Objects.nonNull(maxPrice)) {
            builder.with(productSpecificationFactory.isLessThan("price", maxPrice));
        }
        return builder.build();
    }
}
