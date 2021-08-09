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
import java.math.BigDecimal;

@Data
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProductFormDto extends BaseDto<Product> {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    public ProductFormDto(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    @Override
    public Product convert() {
        return Product.builder()
                .name(this.name)
                .description(this.description)
                .price(this.price).build();
    }
}
