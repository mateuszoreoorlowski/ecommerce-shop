package pl.edu.ecommerceshop.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.inventory.dto.StockMovementResponse;
import pl.edu.ecommerceshop.inventory.repository.StockMovementRepository;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<StockMovementResponse> getStockMovementsList(Long productId, Pageable pageable) {
        Page<StockMovementResponse> page = productId == null
                ? stockMovementRepository.findAll(pageable).map(StockMovementMapper::mapToStockMovementResponse)
                : stockMovementRepository.findByProductId(productId, pageable).map(StockMovementMapper::mapToStockMovementResponse);
        return PageResponse.from(page);
    }

}
