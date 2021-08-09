package br.com.jrafael.catalog.api.dto;

import br.com.jrafael.catalog.model.Product;
import br.com.jrafael.infrastructure.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProductDto extends BaseDto<Product> {

    private String id;

    @Size(max = 255)
    @NotBlank
    @NotNull
    private String name;

    @Size(max = 255)
    @NotBlank
    @NotNull
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    @Override
    public Product convert() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .price(this.price).build();
    }
}
