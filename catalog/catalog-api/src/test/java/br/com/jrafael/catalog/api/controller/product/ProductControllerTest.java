package br.com.jrafael.catalog.api.controller.product;

import br.com.jrafael.catalog.api.CatalogApiApplication;
import br.com.jrafael.catalog.api.dto.ProductDto;
import br.com.jrafael.catalog.api.dto.ProductFormDto;
import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.catalog.repository.product.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CatalogApiApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    private ProductFormDto formDto;

    private ProductDto dto;
    private ProductDto dto2;


    @BeforeEach
    public void setUp() {
        this.dto = new ProductDto();
        this.dto.setId("dad4b00b-c69f-40d3-b209-d389ec10c0faa");
        this.dto.setName("Product 01");
        this.dto.setDescription("Description Product 01");
        this.dto.setPrice(new BigDecimal("200.20"));

        this.dto2 = new ProductDto();
        this.dto2.setId("dad4b00b-c69f-40d3-b209-d389ec10c0bb");
        this.dto2.setName("Product 02");
        this.dto2.setDescription("Description Product 02");
        this.dto2.setPrice(new BigDecimal("20.20"));

        this.formDto = new ProductFormDto();
        this.formDto.setName("Product 01");
        this.formDto.setDescription("Description Product 01");
        this.formDto.setPrice(new BigDecimal("200.20"));
    }

    @Test
    public void postTest() throws Exception {

        given(this.productRepository.save(this.formDto.convert())).willReturn(this.dto.convert());

        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(this.dto.getId())));
    }

    @Test
    public void postNoMandatoryFieldsTest() throws Exception {

        this.formDto.setPrice(null);
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setPrice(new BigDecimal("200.20"));
        this.formDto.setName(null);
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setPrice(new BigDecimal("200.20"));
        this.formDto.setName("Product 01");
        this.formDto.setDescription(null);
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setPrice(null);
        this.formDto.setName(null);
        this.formDto.setDescription(null);
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void postWithMandatoryBlankFieldsTest() throws Exception {
        this.formDto.setName("");
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName("Product 01");
        this.formDto.setDescription("");
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName("");
        this.formDto.setDescription("");
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void postWithZeroAndNegativePriceTest() throws Exception {
        this.formDto.setPrice(new BigDecimal("0"));
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setPrice(new BigDecimal("-000.1"));
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        this.mockMvc
                .perform(post("/products").content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));
    }


    @Test
    public void updateTest() throws Exception {
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        this.formDto.setName("new name");
        this.dto.setName("new name");
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.dto.getId())))
                .andExpect(jsonPath("$.name", is(this.formDto.getName())));
    }

    @Test
    public void updateNoMandatoryFieldsTest() throws Exception {
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));

        this.formDto.setName(null);
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName(this.dto.getName());
        this.formDto.setDescription(null);
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName(this.dto.getName());
        this.formDto.setDescription(this.dto.getDescription());
        this.formDto.setPrice(null);
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName(null);
        this.formDto.setDescription(null);
        this.formDto.setPrice(null);
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));


    }

    @Test
    public void updateWithZeroAndNegativePriceTest() throws Exception {
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));
        this.formDto.setPrice(BigDecimal.ZERO);
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setPrice(new BigDecimal("-1"));
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void updateWithMandatoryBlankFieldsTest() throws Exception {
        given(this.productRepository.save(any(Product.class))).willReturn(this.dto.convert());
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));

        this.formDto.setName("");
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName(this.dto.getName());
        this.formDto.setDescription("");
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));

        this.formDto.setName("");
        this.formDto.setDescription("");
        this.mockMvc
                .perform(put("/products/" + this.dto.getId()).content(this.objectMapper.writeValueAsString(this.formDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void deleteTest() throws Exception {
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));
        this.mockMvc
                .perform(delete("/products/" + this.dto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteWithInvalidIdTest() throws Exception {
        this.mockMvc
                .perform(delete("/products/" + this.dto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void serchByAllResquestParamsTest() throws Exception {
        BigDecimal maxPrice = new BigDecimal("400");
        BigDecimal minPrice = BigDecimal.ZERO;
        this.dto2.setName(this.dto.getName());
        List<Product> products = new ArrayList<>();
        products.add(this.dto.convert());
        products.add(this.dto2.convert());
        given(this.productRepository.findAll(any(Specification.class))).willReturn(products);

        this.mockMvc
                .perform(get("/products/search")
                        .param("max_price", maxPrice.toString())
                        .param("min_price", minPrice.toString())
                        .param("q", this.dto.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        this.mockMvc
                .perform(get("/products/search")
                        .param("min_price", minPrice.toString())
                        .param("q", this.dto.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        this.mockMvc
                .perform(get("/products/search")
                        .param("max_price", maxPrice.toString())
                        .param("q", this.dto.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        this.mockMvc
                .perform(get("/products/search")
                        .param("q", this.dto.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void serchByInvalidParamsMinAndMaxPriceTest() throws Exception {

        this.mockMvc
                .perform(get("/products/search")
                        .param("max_price", "test_erro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mockMvc
                .perform(get("/products/search")
                        .param("min_price", "teste_erro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getByIdTest() throws Exception {
        given(this.productRepository.findById(this.dto.getId())).willReturn(Optional.of(this.dto.convert()));

        this.mockMvc
                .perform(get("/products/"+dto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.dto.getId())));

    }

    @Test
    public void getByInvalidIdTest() throws Exception {
        this.mockMvc
                .perform(get("/products/"+dto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
