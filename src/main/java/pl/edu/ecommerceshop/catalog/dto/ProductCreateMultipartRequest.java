package pl.edu.ecommerceshop.catalog.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record ProductCreateMultipartRequest(
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

        @NotNull
        @Min(0)
        Integer stockQuantity,

        @NotNull
        Long categoryId,

        @NotNull
        MultipartFile image

) {
}