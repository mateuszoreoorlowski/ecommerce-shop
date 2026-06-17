package pl.edu.ecommerceshop.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReceiveStockRequest(
        @Min(1)
        int quantity,

        @NotBlank
        @Size(max = 500)
        String reason
) {
}