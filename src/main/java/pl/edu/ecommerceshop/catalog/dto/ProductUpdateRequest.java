package pl.edu.ecommerceshop.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        @Size(max = 180)
        String name,

        @Size(max = 2000)
        String description,

        @DecimalMin(value = "0.01")
        BigDecimal price,

        Boolean active,
        Long categoryId
) {
}
