package pl.edu.ecommerceshop.catalog.service;

import org.springframework.data.domain.Pageable;
import pl.edu.ecommerceshop.catalog.dto.*;
import pl.edu.ecommerceshop.catalog.model.Product;
import pl.edu.ecommerceshop.common.dto.PageResponse;

public interface ProductService {

    PageResponse<ProductResponse> getProductsList(String query, String categorySlug, Boolean active, Pageable pageable);

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    ProductResponse receiveStock(Long id, ReceiveStockRequest request);

    Product findProduct(Long id);

    ProductResponse getProductById(Long id);

    ProductResponse createProductWithImage(ProductCreateMultipartRequest request);

    ProductResponse updateProductWithImage(Long id, ProductUpdateMultipartRequest request);
}
