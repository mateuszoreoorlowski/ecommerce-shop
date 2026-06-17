package pl.edu.ecommerceshop.inventory.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ecommerceshop.common.dto.PageResponse;
import pl.edu.ecommerceshop.inventory.dto.StockMovementResponse;
import pl.edu.ecommerceshop.inventory.service.StockMovementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping
    public PageResponse<StockMovementResponse> list(
            @RequestParam(required = false)
            @Parameter(description = "Product ID to filter by. If not provided, returns stock movements for all products.")
            Long productId,

            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt")
            Pageable pageable
    ) {
        return stockMovementService.getStockMovementsList(productId, pageable);
    }
}
