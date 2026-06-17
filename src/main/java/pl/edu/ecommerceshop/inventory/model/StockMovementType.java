package pl.edu.ecommerceshop.inventory.model;

public enum StockMovementType {
    INITIAL_STOCK,
    STOCK_RECEIVED,
    RESERVATION_CREATED,
    RESERVATION_RELEASED,
    SALE_COMMITTED,
    STOCK_ADJUSTED,
    RETURN_RECEIVED,
    DAMAGED
}
