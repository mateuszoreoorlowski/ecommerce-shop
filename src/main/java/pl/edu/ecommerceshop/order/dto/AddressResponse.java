package pl.edu.ecommerceshop.order.dto;

public record AddressResponse(
        String street,
        String city,
        String postalCode,
        String country
) {
}
