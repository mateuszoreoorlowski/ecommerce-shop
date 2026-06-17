package pl.edu.ecommerceshop.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.catalog.dto.ProductCreateRequest;
import pl.edu.ecommerceshop.catalog.dto.ProductResponse;
import pl.edu.ecommerceshop.catalog.dto.ProductUpdateRequest;
import pl.edu.ecommerceshop.catalog.dto.ReceiveStockRequest;
import pl.edu.ecommerceshop.catalog.service.ProductService;
import pl.edu.ecommerceshop.common.dto.PageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get products list", description = "Returns paginated and filtered product list.")
    public PageResponse<ProductResponse> list(
            @Parameter(description = "Search phrase used to filter products by name, description or SKU")
            @RequestParam(required = false) String query,

            @Parameter(description = "Category slug, for example: electronics")
            @RequestParam(required = false) String categorySlug,

            @Parameter(description = "Product activity status. Use true for active products, false for inactive products.")
            @RequestParam(required = false) Boolean active,

            @ParameterObject
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        return productService.getProductsList(query, categorySlug, active, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    @PatchMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse receiveStock(@PathVariable Long id, @Valid @RequestBody ReceiveStockRequest request) {
        return productService.receiveStock(id, request);
    }
}
