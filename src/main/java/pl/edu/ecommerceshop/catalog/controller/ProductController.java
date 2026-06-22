package pl.edu.ecommerceshop.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.catalog.dto.*;
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

    @Operation(
            summary = "Create product without image upload",
            description = "Creates product using JSON body. Use this endpoint when imageUrl is already known or image upload is not needed."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    @Operation(
            summary = "Update product without image upload",
            description = "Updates product fields using JSON body. Use this endpoint for changing name, description, price, active status, category or imageUrl."
    )
    @PatchMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse receiveStock(@PathVariable Long id, @Valid @RequestBody ReceiveStockRequest request) {
        return productService.receiveStock(id, request);
    }

    @Operation(
            summary = "Create product with image upload",
            description = "Creates product and uploads image file to Cloudflare R2 using multipart/form-data."
    )
    @PostMapping(
            value = "/with-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createWithImage(
            @Valid @ModelAttribute ProductCreateMultipartRequest request
    ) {
        return productService.createProductWithImage(request);
    }

    @Operation(
            summary = "Update product with image upload",
            description = "Updates product fields and optionally uploads a new image to Cloudflare R2 using multipart/form-data."
    )
    @PatchMapping(
            value = "/{id}/with-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ProductResponse updateWithImage(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductUpdateMultipartRequest request
    ) {
        return productService.updateProductWithImage(id, request);
    }
}
