package pl.edu.ecommerceshop.catalog.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryPatchRequest(
        @Size(max = 120)
        String name,

        @Size(max = 140)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be lowercase kebab-case.")
        String slug
) {
}
