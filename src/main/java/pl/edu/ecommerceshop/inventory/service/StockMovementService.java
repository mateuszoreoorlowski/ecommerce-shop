package pl.edu.ecommerceshop.inventory.service;

import org.springframework.data.domain.Pageable;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.inventory.dto.StockMovementResponse;

public interface StockMovementService {

    PageResponse<StockMovementResponse> getStockMovementsList(Long productId, Pageable pageable);
}
