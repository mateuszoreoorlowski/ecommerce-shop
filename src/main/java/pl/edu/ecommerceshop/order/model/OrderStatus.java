package pl.edu.ecommerceshop.order.model;

public enum OrderStatus {
    NEW,                // zamówienie utworzone
    CONFIRMED,          // zamówienie potwierdzone, można je realizować
    PROCESSING,         // kompletowanie / przygotowanie zamówienia
    READY_TO_SHIP,      // gotowe do wysyłki
    SHIPPED,            // wysłane
    DELIVERED,          // dostarczone do klienta
    COMPLETED,          // zakończone biznesowo
    CANCELLED,          // anulowane
    RETURN_REQUESTED,   // klient zgłosił zwrot
    RETURNED,           // zwrócone
    REFUNDED            // zwrot pieniędzy zakończony
}
