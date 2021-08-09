package br.com.jrafael.catalog.api.controller;

import br.com.jrafael.catalog.api.dto.ProductDto;
import br.com.jrafael.catalog.api.dto.ProductFormDto;
import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.catalog.repository.product.ProductRepository;
import br.com.jrafael.catalog.repository.product.specification.ProductSpecificationBuild;
import br.com.jrafael.infrastructure.controller.CrudController;
import br.com.jrafael.infrastructure.repository.specification.factory.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping(value = "/products")
public class ProductController extends CrudController<Product, ProductDto, ProductFormDto, String> {

    @Autowired
    private SpecificationFactory<Product> productSpecificationFactory;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected ProductDto convert(Product entity) {
        return new ProductDto(entity);
    }

    @GetMapping("/search")
    public List<ProductDto> get(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "min_price", required = false) BigDecimal minPrice,
            @RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {
        ProductSpecificationBuild builder = new ProductSpecificationBuild(productSpecificationFactory);
        List<Product> entities = this.repository.findAll(builder.build(q, maxPrice, minPrice));
        return entities.stream().map(entity -> this.convert(entity)).collect(Collectors.toList());
    }
}
