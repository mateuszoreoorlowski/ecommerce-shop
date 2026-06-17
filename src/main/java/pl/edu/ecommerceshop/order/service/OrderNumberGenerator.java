package pl.edu.ecommerceshop.order.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class OrderNumberGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private final Clock clock;

    public OrderNumberGenerator() {
        this.clock = Clock.systemUTC();
    }

    public String generate() {
        String date = LocalDate.now(clock).format(DATE_FORMATTER);
        int random = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        return "SF-%s-%d".formatted(date, random);
    }
}
