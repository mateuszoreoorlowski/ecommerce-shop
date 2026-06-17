package pl.edu.ecommerceshop.catalog.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank
        @Size(max = 80)
        String sku,

        @NotBlank
        @Size(max = 180)
        String name,

        @NotBlank
        @Size(max = 2000)
        String description,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal price,

        @Min(0)
        int stockQuantity,

        @NotNull
        Long categoryId
) {
}
