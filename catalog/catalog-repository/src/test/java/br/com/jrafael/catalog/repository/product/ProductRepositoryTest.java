package br.com.jrafael.catalog.repository.product;

import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.catalog.repository.configuration.RepositoryTestConfiguration;
import br.com.jrafael.catalog.repository.product.specification.ProductSpecificationBuild;
import br.com.jrafael.infrastructure.repository.specification.factory.SpecificationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(RepositoryTestConfiguration.class)
@ContextConfiguration(classes = {ProductRepository.class})
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SpecificationFactory<Product> productSpecificationFactory;

    private Product product;

    private Product product2;

    @BeforeEach
    public void setUp() {
        this.product = new Product();
        this.product.setName("Product 01");
        this.product.setDescription("Description Product 01");
        this.product.setPrice(new BigDecimal("200.20"));

        this.product2 = new Product();
        this.product2.setName("Product 02");
        this.product2.setDescription("Description Product 02");
        this.product2.setPrice(new BigDecimal("20.30"));
    }

    @Test
    public void saveProductTest() {
        Product resultSave = this.productRepository.save(this.product);
        Optional<Product> result = this.productRepository.findById(resultSave.getId());
        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals(resultSave.getName()));
        assertTrue(result.get().getId().equals(resultSave.getId()));
    }

    @Test()
    public void saveProductWithNoMandatoryFildesTest() {
        this.product.setName(null);
        this.product.setPrice(null);
        this.product.setDescription(null);
        assertThrows(ConstraintViolationException.class, () -> {
            Product resultSave =  this.productRepository.saveAndFlush(this.product);
        });
    }

    @Test
    public void deleteProductTest() {
        Product resultSave =  this.productRepository.save(this.product);
        Optional<Product> result = this.productRepository.findById(resultSave.getId());
        assertTrue(result.isPresent());
        this.productRepository.delete(resultSave);
        result = this.productRepository.findById(resultSave.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void UpdateProductTest() {
        Product resultSave =  this.productRepository.save(this.product);
        Optional<Product> result = this.productRepository.findById(resultSave.getId());
        assertTrue(result.isPresent());
        resultSave.setName("atualizado");
        this.productRepository.save(resultSave);
        Optional<Product> resultUpdated = this.productRepository.findById(resultSave.getId());
        assertTrue(resultUpdated.isPresent());
        assertTrue(resultUpdated.get().getName().equals(resultSave.getName()));
    }

    @Test
    public void ListAllProductTest() {
        Product resultSave = this.productRepository.save(this.product);
        Product resultSave2 = this.productRepository.save(this.product2);
        Optional<Product> result = this.productRepository.findById(resultSave.getId());
        Optional<Product> result2 = this.productRepository.findById(resultSave2.getId());
        assertTrue(result.isPresent());
        assertTrue(result2.isPresent());

        List<Product> products = this.productRepository.findAll();
        assertTrue(!products.isEmpty());
        assertTrue(products.size() == 2);
    }

    @Test
    public void ListAllProductWithSpecificationTest() {
        this.product.setName(this.product2.getName());
        Product resultSave = this.productRepository.save(this.product);
        Product resultSave2 = this.productRepository.save(this.product2);
        Optional<Product> result = this.productRepository.findById(resultSave.getId());
        Optional<Product> result2 = this.productRepository.findById(resultSave2.getId());
        assertTrue(result.isPresent());
        assertTrue(result2.isPresent());
        ProductSpecificationBuild builder = new ProductSpecificationBuild(productSpecificationFactory);
        List<Product> products = this.productRepository.findAll(builder.build(resultSave.getName(), new BigDecimal("6000"), BigDecimal.ZERO));
        assertTrue(!products.isEmpty());
        assertTrue(products.size() == 2);
    }
}
