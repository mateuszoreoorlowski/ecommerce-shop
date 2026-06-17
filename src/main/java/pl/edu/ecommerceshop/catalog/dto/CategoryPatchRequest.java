package pl.edu.ecommerceshop.catalog.dto;

public record CategoryPatchRequest(
        String name,
        String slug
) {
}
