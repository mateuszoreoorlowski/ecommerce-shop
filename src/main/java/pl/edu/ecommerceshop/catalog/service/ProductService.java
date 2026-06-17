package pl.edu.ecommerceshop.catalog.service;

import org.springframework.data.domain.Pageable;
import pl.edu.ecommerceshop.catalog.dto.ProductCreateRequest;
import pl.edu.ecommerceshop.catalog.dto.ProductResponse;
import pl.edu.ecommerceshop.catalog.dto.ProductUpdateRequest;
import pl.edu.ecommerceshop.catalog.dto.ReceiveStockRequest;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.common.dto.PageResponse;

public interface ProductService {

    PageResponse<ProductResponse> getProductsList(String query, String categorySlug, Boolean active, Pageable pageable);

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    ProductResponse receiveStock(Long id, ReceiveStockRequest request);

    Product findProduct(Long id);

    ProductResponse getProductById(Long id);
}
