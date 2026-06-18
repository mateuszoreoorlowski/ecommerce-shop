INSERT INTO categories (name, slug)
VALUES ('Elektronika', 'elektronika'),
       ('Książki', 'ksiazki'),
       ('Dom i kuchnia', 'dom-i-kuchnia'),
       ('Sport i turystyka', 'sport-i-turystyka'),
       ('Uroda i zdrowie', 'uroda-i-zdrowie'),
       ('Moda', 'moda'),
       ('Zabawki', 'zabawki'),
       ('Biuro', 'biuro'),
       ('Ogród', 'ogrod'),
       ('Motoryzacja', 'motoryzacja');

-- =========================
-- PRODUCTS
-- =========================

INSERT INTO products
(sku, name, description, price, stock_quantity, reserved_quantity, active, category_id, version, created_at, updated_at)
VALUES ('EL-1001', 'Laptop Lenovo IdeaPad 15', 'Laptop do pracy, nauki i codziennego użytku.', 3299.00, 25, 3, true,
        (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:00:00', '2026-06-10 12:00:00'),
       ('EL-1002', 'Smartfon Samsung Galaxy A55', 'Smartfon z ekranem AMOLED i pojemną baterią.', 1799.00, 40, 5, true,
        (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:05:00', '2026-06-10 12:00:00'),
       ('EL-1003', 'Słuchawki Sony WH-CH720N', 'Bezprzewodowe słuchawki z aktywną redukcją hałasu.', 429.99, 80, 8,
        true, (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:10:00', '2026-06-10 12:00:00'),
       ('EL-1004', 'Monitor LG 27 cali', 'Monitor IPS 27 cali do pracy i rozrywki.', 899.00, 30, 2, true,
        (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:15:00', '2026-06-10 12:00:00'),
       ('EL-1005', 'Klawiatura Logitech MX Keys', 'Bezprzewodowa klawiatura premium.', 479.00, 50, 4, true,
        (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:20:00', '2026-06-10 12:00:00'),
       ('EL-1006', 'Mysz Logitech MX Master 3S', 'Ergonomiczna mysz do pracy biurowej.', 419.00, 55, 6, true,
        (SELECT id FROM categories WHERE slug = 'elektronika'), 0, '2026-05-01 09:25:00', '2026-06-10 12:00:00'),

       ('BK-2001', 'Czysty kod', 'Książka o dobrych praktykach programowania.', 89.99, 120, 10, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:00:00', '2026-06-09 11:00:00'),
       ('BK-2002', 'Wzorce projektowe', 'Opis popularnych wzorców projektowych.', 119.99, 90, 5, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:05:00', '2026-06-09 11:00:00'),
       ('BK-2003', 'Java. Efektywne programowanie', 'Zaawansowane techniki programowania w Javie.', 139.99, 70, 3, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:10:00', '2026-06-09 11:00:00'),
       ('BK-2004', 'Algorytmy. Ilustrowany przewodnik', 'Przystępne wprowadzenie do algorytmów.', 59.99, 110, 6, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:15:00', '2026-06-09 11:00:00'),
       ('BK-2005', 'Bazy danych. Projektowanie', 'Podstawy projektowania relacyjnych baz danych.', 79.99, 85, 7, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:20:00', '2026-06-09 11:00:00'),
       ('BK-2006', 'Spring Boot w praktyce', 'Praktyczny przewodnik po aplikacjach Spring Boot.', 99.99, 95, 4, true,
        (SELECT id FROM categories WHERE slug = 'ksiazki'), 0, '2026-05-02 10:25:00', '2026-06-09 11:00:00'),

       ('HK-3001', 'Ekspres do kawy DeLonghi', 'Automatyczny ekspres do kawy z młynkiem.', 1299.00, 18, 2, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:00:00', '2026-06-08 13:00:00'),
       ('HK-3002', 'Blender Bosch', 'Blender kielichowy do koktajli i zup krem.', 349.00, 45, 4, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:05:00', '2026-06-08 13:00:00'),
       ('HK-3003', 'Czajnik Philips', 'Elektryczny czajnik o pojemności 1.7 l.', 149.99, 65, 7, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:10:00', '2026-06-08 13:00:00'),
       ('HK-3004', 'Patelnia Tefal', 'Patelnia z powłoką nieprzywierającą.', 129.90, 100, 9, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:15:00', '2026-06-08 13:00:00'),
       ('HK-3005', 'Komplet noży Fiskars', 'Zestaw noży kuchennych ze stojakiem.', 249.00, 38, 3, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:20:00', '2026-06-08 13:00:00'),
       ('HK-3006', 'Robot kuchenny Kenwood', 'Wielofunkcyjny robot kuchenny.', 899.00, 22, 1, true,
        (SELECT id FROM categories WHERE slug = 'dom-i-kuchnia'), 0, '2026-05-03 08:25:00', '2026-06-08 13:00:00'),

       ('SP-4001', 'Rower trekkingowy Kross', 'Rower trekkingowy do miasta i dłuższych tras.', 2199.00, 12, 1, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:00:00', '2026-06-07 14:00:00'),
       ('SP-4002', 'Mata do jogi', 'Antypoślizgowa mata treningowa.', 79.99, 130, 12, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:05:00', '2026-06-07 14:00:00'),
       ('SP-4003', 'Hantle 2x10 kg', 'Zestaw hantli do treningu siłowego.', 199.00, 50, 5, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:10:00', '2026-06-07 14:00:00'),
       ('SP-4004', 'Plecak turystyczny 60L', 'Pojemny plecak na wyprawy turystyczne.', 329.00, 34, 3, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:15:00', '2026-06-07 14:00:00'),
       ('SP-4005', 'Buty biegowe Nike', 'Lekkie buty do biegania po asfalcie.', 449.00, 44, 4, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:20:00', '2026-06-07 14:00:00'),
       ('SP-4006', 'Bidon termiczny', 'Bidon utrzymujący temperaturę napoju.', 49.99, 160, 15, true,
        (SELECT id FROM categories WHERE slug = 'sport-i-turystyka'), 0, '2026-05-04 11:25:00', '2026-06-07 14:00:00'),

       ('BZ-5001', 'Szczoteczka Oral-B', 'Elektryczna szczoteczka do zębów.', 249.00, 70, 5, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:00:00', '2026-06-06 15:00:00'),
       ('BZ-5002', 'Suszarka Philips', 'Suszarka z regulacją temperatury.', 199.00, 60, 4, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:05:00', '2026-06-06 15:00:00'),
       ('BZ-5003', 'Krem nawilżający', 'Krem do codziennej pielęgnacji twarzy.', 39.99, 180, 20, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:10:00', '2026-06-06 15:00:00'),
       ('BZ-5004', 'Zestaw witamin', 'Zestaw witamin dla osób aktywnych.', 59.99, 150, 10, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:15:00', '2026-06-06 15:00:00'),
       ('BZ-5005', 'Maszynka do golenia Braun', 'Elektryczna maszynka do golenia.', 349.00, 40, 4, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:20:00', '2026-06-06 15:00:00'),
       ('BZ-5006', 'Waga łazienkowa Xiaomi', 'Inteligentna waga z pomiarem składu ciała.', 89.99, 90, 6, true,
        (SELECT id FROM categories WHERE slug = 'uroda-i-zdrowie'), 0, '2026-05-05 12:25:00', '2026-06-06 15:00:00'),

       ('FA-6001', 'Bluza męska', 'Bawełniana bluza z kapturem.', 159.99, 80, 8, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:00:00', '2026-06-05 16:00:00'),
       ('FA-6002', 'Kurtka damska', 'Lekka kurtka przejściowa.', 299.00, 35, 3, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:05:00', '2026-06-05 16:00:00'),
       ('FA-6003', 'Sneakersy unisex', 'Wygodne buty miejskie.', 249.00, 55, 5, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:10:00', '2026-06-05 16:00:00'),
       ('FA-6004', 'Pasek skórzany', 'Elegancki pasek ze skóry naturalnej.', 89.99, 100, 7, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:15:00', '2026-06-05 16:00:00'),
       ('FA-6005', 'Plecak miejski', 'Plecak do pracy, szkoły i podróży.', 149.00, 75, 6, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:20:00', '2026-06-05 16:00:00'),
       ('FA-6006', 'Koszulka basic', 'Prosta koszulka bawełniana.', 49.99, 200, 18, true,
        (SELECT id FROM categories WHERE slug = 'moda'), 0, '2026-05-06 10:25:00', '2026-06-05 16:00:00'),

       ('TO-7001', 'Klocki konstrukcyjne', 'Zestaw kreatywnych klocków dla dzieci.', 179.99, 65, 5, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:00:00', '2026-06-04 10:00:00'),
       ('TO-7002', 'Gra planszowa', 'Rodzinna gra strategiczna.', 129.99, 72, 6, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:05:00', '2026-06-04 10:00:00'),
       ('TO-7003', 'Puzzle 1000 elementów', 'Puzzle krajobrazowe dla dorosłych.', 49.99, 95, 8, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:10:00', '2026-06-04 10:00:00'),
       ('TO-7004', 'Pluszowy miś', 'Miękka maskotka dla najmłodszych.', 69.99, 88, 7, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:15:00', '2026-06-04 10:00:00'),
       ('TO-7005', 'Zdalnie sterowany samochód', 'Samochód RC z akumulatorem.', 249.99, 30, 2, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:20:00', '2026-06-04 10:00:00'),
       ('TO-7006', 'Zestaw kreatywny', 'Zestaw plastyczny dla dzieci.', 39.99, 140, 12, true,
        (SELECT id FROM categories WHERE slug = 'zabawki'), 0, '2026-05-07 09:25:00', '2026-06-04 10:00:00'),

       ('OF-8001', 'Fotel biurowy', 'Ergonomiczny fotel do pracy przy biurku.', 699.00, 28, 3, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:00:00', '2026-06-03 10:30:00'),
       ('OF-8002', 'Biurko regulowane', 'Biurko z elektryczną regulacją wysokości.', 1199.00, 15, 1, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:05:00', '2026-06-03 10:30:00'),
       ('OF-8003', 'Papier A4 ryza', 'Ryza papieru biurowego 500 arkuszy.', 24.99, 300, 30, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:10:00', '2026-06-03 10:30:00'),
       ('OF-8004', 'Notes A5', 'Notes w twardej oprawie.', 14.99, 250, 20, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:15:00', '2026-06-03 10:30:00'),
       ('OF-8005', 'Pióro Parker', 'Eleganckie pióro wieczne.', 119.00, 70, 5, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:20:00', '2026-06-03 10:30:00'),
       ('OF-8006', 'Lampka biurkowa LED', 'Lampka LED z regulacją jasności.', 129.99, 65, 6, true,
        (SELECT id FROM categories WHERE slug = 'biuro'), 0, '2026-05-08 08:25:00', '2026-06-03 10:30:00'),

       ('GD-9001', 'Kosiarka elektryczna', 'Kosiarka do ogrodów średniej wielkości.', 649.00, 20, 2, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:00:00', '2026-06-02 09:00:00'),
       ('GD-9002', 'Sekator Fiskars', 'Sekator ogrodowy do przycinania gałęzi.', 89.00, 90, 6, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:05:00', '2026-06-02 09:00:00'),
       ('GD-9003', 'Grill ogrodowy', 'Grill węglowy z pokrywą.', 799.00, 17, 1, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:10:00', '2026-06-02 09:00:00'),
       ('GD-9004', 'Zestaw mebli balkonowych', 'Stolik i dwa krzesła na balkon.', 999.00, 14, 1, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:15:00', '2026-06-02 09:00:00'),
       ('GD-9005', 'Donica ceramiczna', 'Dekoracyjna donica do kwiatów.', 59.99, 120, 10, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:20:00', '2026-06-02 09:00:00'),
       ('GD-9006', 'Nawóz uniwersalny', 'Uniwersalny nawóz do roślin ogrodowych.', 29.99, 180, 15, true,
        (SELECT id FROM categories WHERE slug = 'ogrod'), 0, '2026-05-09 12:25:00', '2026-06-02 09:00:00'),

       ('AU-10001', 'Kompresor samochodowy', 'Przenośny kompresor do opon.', 159.00, 55, 4, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:00:00', '2026-06-01 12:00:00'),
       ('AU-10002', 'Kamera samochodowa', 'Wideorejestrator Full HD.', 349.00, 45, 3, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:05:00', '2026-06-01 12:00:00'),
       ('AU-10003', 'Uchwyt na telefon', 'Uchwyt samochodowy na telefon.', 39.99, 150, 12, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:10:00', '2026-06-01 12:00:00'),
       ('AU-10004', 'Dywaniki gumowe', 'Komplet gumowych dywaników samochodowych.', 119.99, 80, 8, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:15:00', '2026-06-01 12:00:00'),
       ('AU-10005', 'Prostownik akumulatorowy', 'Prostownik do ładowania akumulatorów.', 229.00, 35, 2, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:20:00', '2026-06-01 12:00:00'),
       ('AU-10006', 'Apteczka samochodowa', 'Podstawowa apteczka do auta.', 34.99, 200, 15, true,
        (SELECT id FROM categories WHERE slug = 'motoryzacja'), 0, '2026-05-10 14:25:00', '2026-06-01 12:00:00');

-- =========================
-- USERS
-- =========================

INSERT INTO users
(email, password_hash, first_name, last_name, role, active, created_at, updated_at)
VALUES ('admin@shop.pl', 'demo_hash_admin_001', 'Adam', 'Administrator', 'ADMIN', true, '2026-05-01 08:00:00',
        '2026-06-01 08:00:00'),
       ('seller@shop.pl', 'demo_hash_seller_001', 'Sandra', 'Sprzedawca', 'SELLER', true, '2026-05-01 08:10:00',
        '2026-06-01 08:00:00'),
       ('anna.kowalska@example.com', 'demo_hash_user_001', 'Anna', 'Kowalska', 'CUSTOMER', true, '2026-05-02 09:00:00',
        '2026-06-01 10:00:00'),
       ('jan.nowak@example.com', 'demo_hash_user_002', 'Jan', 'Nowak', 'CUSTOMER', true, '2026-05-02 09:10:00',
        '2026-06-01 10:00:00'),
       ('marta.zielinska@example.com', 'demo_hash_user_003', 'Marta', 'Zielińska', 'CUSTOMER', true,
        '2026-05-03 09:00:00', '2026-06-01 10:00:00'),
       ('piotr.wisniewski@example.com', 'demo_hash_user_004', 'Piotr', 'Wiśniewski', 'CUSTOMER', true,
        '2026-05-03 09:10:00', '2026-06-01 10:00:00'),
       ('karolina.wojcik@example.com', 'demo_hash_user_005', 'Karolina', 'Wójcik', 'CUSTOMER', true,
        '2026-05-04 09:00:00', '2026-06-01 10:00:00'),
       ('tomasz.kaminski@example.com', 'demo_hash_user_006', 'Tomasz', 'Kamiński', 'CUSTOMER', true,
        '2026-05-04 09:10:00', '2026-06-01 10:00:00'),
       ('ewa.lewandowska@example.com', 'demo_hash_user_007', 'Ewa', 'Lewandowska', 'CUSTOMER', true,
        '2026-05-05 09:00:00', '2026-06-01 10:00:00'),
       ('michal.dabrowski@example.com', 'demo_hash_user_008', 'Michał', 'Dąbrowski', 'CUSTOMER', true,
        '2026-05-05 09:10:00', '2026-06-01 10:00:00'),
       ('agnieszka.mazur@example.com', 'demo_hash_user_009', 'Agnieszka', 'Mazur', 'CUSTOMER', true,
        '2026-05-06 09:00:00', '2026-06-01 10:00:00'),
       ('pawel.kaczmarek@example.com', 'demo_hash_user_010', 'Paweł', 'Kaczmarek', 'CUSTOMER', true,
        '2026-05-06 09:10:00', '2026-06-01 10:00:00'),
       ('monika.krol@example.com', 'demo_hash_user_011', 'Monika', 'Król', 'CUSTOMER', true, '2026-05-07 09:00:00',
        '2026-06-01 10:00:00'),
       ('adam.wieczorek@example.com', 'demo_hash_user_012', 'Adam', 'Wieczorek', 'CUSTOMER', true,
        '2026-05-07 09:10:00', '2026-06-01 10:00:00'),
       ('natalia.pawlak@example.com', 'demo_hash_user_013', 'Natalia', 'Pawlak', 'CUSTOMER', true,
        '2026-05-08 09:00:00', '2026-06-01 10:00:00');

-- =========================
-- CARTS
-- =========================

INSERT INTO carts
    (customer_email, status, created_at, updated_at)
VALUES ('anna.kowalska@example.com', 'ACTIVE', '2026-06-12 10:00:00', '2026-06-12 10:20:00'),
       ('jan.nowak@example.com', 'ACTIVE', '2026-06-12 11:00:00', '2026-06-12 11:15:00'),
       ('marta.zielinska@example.com', 'ABANDONED', '2026-06-11 09:30:00', '2026-06-11 10:00:00'),
       ('piotr.wisniewski@example.com', 'CHECKED_OUT', '2026-06-10 13:00:00', '2026-06-10 13:45:00'),
       ('karolina.wojcik@example.com', 'CHECKED_OUT', '2026-06-09 14:00:00', '2026-06-09 14:30:00'),
       ('tomasz.kaminski@example.com', 'ABANDONED', '2026-06-08 17:00:00', '2026-06-08 17:20:00'),
       ('ewa.lewandowska@example.com', 'ACTIVE', '2026-06-13 08:30:00', '2026-06-13 08:45:00'),
       ('michal.dabrowski@example.com', 'CHECKED_OUT', '2026-06-07 12:00:00', '2026-06-07 12:40:00'),
       ('agnieszka.mazur@example.com', 'CHECKED_OUT', '2026-06-06 16:00:00', '2026-06-06 16:25:00'),
       ('pawel.kaczmarek@example.com', 'ACTIVE', '2026-06-14 10:30:00', '2026-06-14 10:40:00'),
       ('monika.krol@example.com', 'CHECKED_OUT', '2026-06-05 09:00:00', '2026-06-05 09:20:00'),
       ('natalia.pawlak@example.com', 'ACTIVE', '2026-06-14 13:00:00', '2026-06-14 13:12:00');

-- =========================
-- CART ITEMS
-- =========================

INSERT INTO cart_items
    (cart_id, product_id, quantity, unit_price_snapshot)
VALUES ((SELECT id
         FROM carts
         WHERE customer_email = 'anna.kowalska@example.com'
           AND created_at = '2026-06-12 10:00:00'),
        (SELECT id FROM products WHERE sku = 'EL-1003'), 1, 429.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'anna.kowalska@example.com'
           AND created_at = '2026-06-12 10:00:00'),
        (SELECT id FROM products WHERE sku = 'BK-2001'), 2, 89.99),

       ((SELECT id FROM carts WHERE customer_email = 'jan.nowak@example.com' AND created_at = '2026-06-12 11:00:00'),
        (SELECT id FROM products WHERE sku = 'HK-3003'), 1, 149.99),
       ((SELECT id FROM carts WHERE customer_email = 'jan.nowak@example.com' AND created_at = '2026-06-12 11:00:00'),
        (SELECT id FROM products WHERE sku = 'HK-3004'), 2, 129.90),
       ((SELECT id FROM carts WHERE customer_email = 'jan.nowak@example.com' AND created_at = '2026-06-12 11:00:00'),
        (SELECT id FROM products WHERE sku = 'OF-8003'), 3, 24.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'marta.zielinska@example.com'
           AND created_at = '2026-06-11 09:30:00'),
        (SELECT id FROM products WHERE sku = 'FA-6002'), 1, 299.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'marta.zielinska@example.com'
           AND created_at = '2026-06-11 09:30:00'),
        (SELECT id FROM products WHERE sku = 'FA-6004'), 1, 89.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'piotr.wisniewski@example.com'
           AND created_at = '2026-06-10 13:00:00'),
        (SELECT id FROM products WHERE sku = 'EL-1001'), 1, 3299.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'piotr.wisniewski@example.com'
           AND created_at = '2026-06-10 13:00:00'),
        (SELECT id FROM products WHERE sku = 'EL-1005'), 1, 479.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'piotr.wisniewski@example.com'
           AND created_at = '2026-06-10 13:00:00'),
        (SELECT id FROM products WHERE sku = 'EL-1006'), 1, 419.00),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'karolina.wojcik@example.com'
           AND created_at = '2026-06-09 14:00:00'),
        (SELECT id FROM products WHERE sku = 'TO-7001'), 2, 179.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'karolina.wojcik@example.com'
           AND created_at = '2026-06-09 14:00:00'),
        (SELECT id FROM products WHERE sku = 'TO-7002'), 1, 129.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'karolina.wojcik@example.com'
           AND created_at = '2026-06-09 14:00:00'),
        (SELECT id FROM products WHERE sku = 'TO-7003'), 1, 49.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'tomasz.kaminski@example.com'
           AND created_at = '2026-06-08 17:00:00'),
        (SELECT id FROM products WHERE sku = 'SP-4001'), 1, 2199.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'tomasz.kaminski@example.com'
           AND created_at = '2026-06-08 17:00:00'),
        (SELECT id FROM products WHERE sku = 'SP-4006'), 2, 49.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'ewa.lewandowska@example.com'
           AND created_at = '2026-06-13 08:30:00'),
        (SELECT id FROM products WHERE sku = 'BZ-5001'), 1, 249.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'ewa.lewandowska@example.com'
           AND created_at = '2026-06-13 08:30:00'),
        (SELECT id FROM products WHERE sku = 'BZ-5003'), 2, 39.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'michal.dabrowski@example.com'
           AND created_at = '2026-06-07 12:00:00'),
        (SELECT id FROM products WHERE sku = 'GD-9003'), 1, 799.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'michal.dabrowski@example.com'
           AND created_at = '2026-06-07 12:00:00'),
        (SELECT id FROM products WHERE sku = 'GD-9005'), 4, 59.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'agnieszka.mazur@example.com'
           AND created_at = '2026-06-06 16:00:00'),
        (SELECT id FROM products WHERE sku = 'BK-2002'), 1, 119.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'agnieszka.mazur@example.com'
           AND created_at = '2026-06-06 16:00:00'),
        (SELECT id FROM products WHERE sku = 'BK-2003'), 1, 139.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'agnieszka.mazur@example.com'
           AND created_at = '2026-06-06 16:00:00'),
        (SELECT id FROM products WHERE sku = 'BK-2005'), 1, 79.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'pawel.kaczmarek@example.com'
           AND created_at = '2026-06-14 10:30:00'),
        (SELECT id FROM products WHERE sku = 'AU-10002'), 1, 349.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'pawel.kaczmarek@example.com'
           AND created_at = '2026-06-14 10:30:00'),
        (SELECT id FROM products WHERE sku = 'AU-10003'), 2, 39.99),

       ((SELECT id FROM carts WHERE customer_email = 'monika.krol@example.com' AND created_at = '2026-06-05 09:00:00'),
        (SELECT id FROM products WHERE sku = 'OF-8001'), 1, 699.00),
       ((SELECT id FROM carts WHERE customer_email = 'monika.krol@example.com' AND created_at = '2026-06-05 09:00:00'),
        (SELECT id FROM products WHERE sku = 'OF-8004'), 5, 14.99),
       ((SELECT id FROM carts WHERE customer_email = 'monika.krol@example.com' AND created_at = '2026-06-05 09:00:00'),
        (SELECT id FROM products WHERE sku = 'OF-8006'), 2, 129.99),

       ((SELECT id
         FROM carts
         WHERE customer_email = 'natalia.pawlak@example.com'
           AND created_at = '2026-06-14 13:00:00'),
        (SELECT id FROM products WHERE sku = 'SP-4002'), 2, 79.99),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'natalia.pawlak@example.com'
           AND created_at = '2026-06-14 13:00:00'),
        (SELECT id FROM products WHERE sku = 'SP-4003'), 1, 199.00),
       ((SELECT id
         FROM carts
         WHERE customer_email = 'natalia.pawlak@example.com'
           AND created_at = '2026-06-14 13:00:00'),
        (SELECT id FROM products WHERE sku = 'SP-4005'), 1, 449.00);

-- =========================
-- ORDERS
-- =========================

INSERT INTO orders
(order_number, customer_name, customer_email, customer_phone, status, payment_status, total_price, created_at,
 updated_at, paid_at, cancelled_at,
 delivery_street, delivery_city, delivery_postal_code, delivery_country)
VALUES ('ORD-2026-0001', 'Anna Kowalska', 'anna.kowalska@example.com', '+48 501 100 101', 'DELIVERED', 'PAID', 609.97,
        '2026-06-02 10:00:00', '2026-06-05 15:00:00', '2026-06-02 10:03:00', NULL, 'Kwiatowa 12', 'Warszawa', '00-001',
        'Polska'),
       ('ORD-2026-0002', 'Jan Nowak', 'jan.nowak@example.com', '+48 501 100 102', 'SHIPPED', 'PAID', 484.76,
        '2026-06-03 11:00:00', '2026-06-04 12:00:00', '2026-06-03 11:04:00', NULL, 'Długa 5', 'Kraków', '30-001',
        'Polska'),
       ('ORD-2026-0003', 'Marta Zielińska', 'marta.zielinska@example.com', '+48 501 100 103', 'PROCESSING', 'PAID',
        538.96, '2026-06-04 09:30:00', '2026-06-04 10:20:00', '2026-06-04 09:35:00', NULL, 'Leśna 8', 'Gdańsk',
        '80-001', 'Polska'),
       ('ORD-2026-0004', 'Piotr Wiśniewski', 'piotr.wisniewski@example.com', '+48 501 100 104', 'PROCESSING', 'PAID',
        4197.00, '2026-06-05 13:00:00', '2026-06-05 13:20:00', '2026-06-05 13:05:00', NULL, 'Słoneczna 21', 'Poznań',
        '60-001', 'Polska'),
       ('ORD-2026-0005', 'Karolina Wójcik', 'karolina.wojcik@example.com', '+48 501 100 105', 'DELIVERED', 'PAID',
        539.96, '2026-06-06 14:00:00', '2026-06-09 16:00:00', '2026-06-06 14:05:00', NULL, 'Polna 4', 'Wrocław',
        '50-001', 'Polska'),
       ('ORD-2026-0006', 'Tomasz Kamiński', 'tomasz.kaminski@example.com', '+48 501 100 106', 'CANCELLED', 'CANCELLED',
        2298.98, '2026-06-07 17:00:00', '2026-06-07 17:30:00', NULL, '2026-06-07 17:30:00', 'Krótka 9', 'Łódź',
        '90-001', 'Polska'),
       ('ORD-2026-0007', 'Ewa Lewandowska', 'ewa.lewandowska@example.com', '+48 501 100 107', 'NEW', 'PENDING', 418.97,
        '2026-06-08 08:30:00', '2026-06-08 08:45:00', NULL, NULL, 'Jasna 7', 'Katowice', '40-001', 'Polska'),
       ('ORD-2026-0008', 'Michał Dąbrowski', 'michal.dabrowski@example.com', '+48 501 100 108', 'PROCESSING', 'PAID',
        1098.94, '2026-06-09 12:00:00', '2026-06-09 12:40:00', '2026-06-09 12:05:00', NULL, 'Ogrodowa 18', 'Lublin',
        '20-001', 'Polska'),
       ('ORD-2026-0009', 'Agnieszka Mazur', 'agnieszka.mazur@example.com', '+48 501 100 109', 'DELIVERED', 'PAID',
        339.97, '2026-06-10 16:00:00', '2026-06-13 13:00:00', '2026-06-10 16:03:00', NULL, 'Szkolna 2', 'Rzeszów',
        '35-001', 'Polska'),
       ('ORD-2026-0010', 'Paweł Kaczmarek', 'pawel.kaczmarek@example.com', '+48 501 100 110', 'AWAITING_PAYMENT',
        'FAILED', 463.97, '2026-06-11 10:30:00', '2026-06-11 10:40:00', NULL, NULL, 'Lipowa 6', 'Szczecin', '70-001',
        'Polska'),
       ('ORD-2026-0011', 'Monika Król', 'monika.krol@example.com', '+48 501 100 111', 'SHIPPED', 'PAID', 1033.93,
        '2026-06-12 09:00:00', '2026-06-13 09:20:00', '2026-06-12 09:04:00', NULL, 'Brzozowa 11', 'Bydgoszcz', '85-001',
        'Polska'),
       ('ORD-2026-0012', 'Adam Wieczorek', 'adam.wieczorek@example.com', '+48 501 100 112', 'PROCESSING', 'PAID',
        1398.99, '2026-06-13 09:10:00', '2026-06-13 09:35:00', '2026-06-13 09:14:00', NULL, 'Cicha 3', 'Toruń',
        '87-100', 'Polska'),
       ('ORD-2026-0013', 'Natalia Pawlak', 'natalia.pawlak@example.com', '+48 501 100 113', 'NEW', 'PENDING', 807.98,
        '2026-06-14 13:00:00', '2026-06-14 13:15:00', NULL, NULL, 'Sportowa 14', 'Opole', '45-001', 'Polska'),
       ('ORD-2026-0014', 'Łukasz Grabowski', 'lukasz.grabowski@example.com', '+48 501 100 114', 'PROCESSING', 'PAID',
        3597.00, '2026-06-15 15:20:00', '2026-06-15 15:45:00', '2026-06-15 15:24:00', NULL, 'Nowa 22', 'Białystok',
        '15-001', 'Polska'),
       ('ORD-2026-0015', 'Julia Sikora', 'julia.sikora@example.com', '+48 501 100 115', 'CANCELLED', 'REFUNDED', 398.97,
        '2026-06-16 18:00:00', '2026-06-16 19:10:00', '2026-06-16 18:05:00', '2026-06-16 19:10:00', 'Morska 19',
        'Gdynia', '81-001', 'Polska');

-- =========================
-- ORDER ITEMS
-- =========================

INSERT INTO order_items
(order_id, product_id, product_sku, product_name, quantity, unit_price, line_total)
VALUES ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0001'), (SELECT id FROM products WHERE sku = 'EL-1003'),
        'EL-1003', 'Słuchawki Sony WH-CH720N', 1, 429.99, 429.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0001'), (SELECT id FROM products WHERE sku = 'BK-2001'),
        'BK-2001', 'Czysty kod', 2, 89.99, 179.98),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0002'), (SELECT id FROM products WHERE sku = 'HK-3003'),
        'HK-3003', 'Czajnik Philips', 1, 149.99, 149.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0002'), (SELECT id FROM products WHERE sku = 'HK-3004'),
        'HK-3004', 'Patelnia Tefal', 2, 129.90, 259.80),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0002'), (SELECT id FROM products WHERE sku = 'OF-8003'),
        'OF-8003', 'Papier A4 ryza', 3, 24.99, 74.97),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0003'), (SELECT id FROM products WHERE sku = 'FA-6002'),
        'FA-6002', 'Kurtka damska', 1, 299.00, 299.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0003'), (SELECT id FROM products WHERE sku = 'FA-6004'),
        'FA-6004', 'Pasek skórzany', 1, 89.99, 89.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0003'), (SELECT id FROM products WHERE sku = 'FA-6006'),
        'FA-6006', 'Koszulka basic', 3, 49.99, 149.97),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0004'), (SELECT id FROM products WHERE sku = 'EL-1001'),
        'EL-1001', 'Laptop Lenovo IdeaPad 15', 1, 3299.00, 3299.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0004'), (SELECT id FROM products WHERE sku = 'EL-1005'),
        'EL-1005', 'Klawiatura Logitech MX Keys', 1, 479.00, 479.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0004'), (SELECT id FROM products WHERE sku = 'EL-1006'),
        'EL-1006', 'Mysz Logitech MX Master 3S', 1, 419.00, 419.00),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0005'), (SELECT id FROM products WHERE sku = 'TO-7001'),
        'TO-7001', 'Klocki konstrukcyjne', 2, 179.99, 359.98),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0005'), (SELECT id FROM products WHERE sku = 'TO-7002'),
        'TO-7002', 'Gra planszowa', 1, 129.99, 129.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0005'), (SELECT id FROM products WHERE sku = 'TO-7003'),
        'TO-7003', 'Puzzle 1000 elementów', 1, 49.99, 49.99),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0006'), (SELECT id FROM products WHERE sku = 'SP-4001'),
        'SP-4001', 'Rower trekkingowy Kross', 1, 2199.00, 2199.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0006'), (SELECT id FROM products WHERE sku = 'SP-4006'),
        'SP-4006', 'Bidon termiczny', 2, 49.99, 99.98),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0007'), (SELECT id FROM products WHERE sku = 'BZ-5001'),
        'BZ-5001', 'Szczoteczka Oral-B', 1, 249.00, 249.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0007'), (SELECT id FROM products WHERE sku = 'BZ-5003'),
        'BZ-5003', 'Krem nawilżający', 2, 39.99, 79.98),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0007'), (SELECT id FROM products WHERE sku = 'BZ-5006'),
        'BZ-5006', 'Waga łazienkowa Xiaomi', 1, 89.99, 89.99),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0008'), (SELECT id FROM products WHERE sku = 'GD-9003'),
        'GD-9003', 'Grill ogrodowy', 1, 799.00, 799.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0008'), (SELECT id FROM products WHERE sku = 'GD-9005'),
        'GD-9005', 'Donica ceramiczna', 4, 59.99, 239.96),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0008'), (SELECT id FROM products WHERE sku = 'GD-9006'),
        'GD-9006', 'Nawóz uniwersalny', 2, 29.99, 59.98),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0009'), (SELECT id FROM products WHERE sku = 'BK-2002'),
        'BK-2002', 'Wzorce projektowe', 1, 119.99, 119.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0009'), (SELECT id FROM products WHERE sku = 'BK-2003'),
        'BK-2003', 'Java. Efektywne programowanie', 1, 139.99, 139.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0009'), (SELECT id FROM products WHERE sku = 'BK-2005'),
        'BK-2005', 'Bazy danych. Projektowanie', 1, 79.99, 79.99),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0010'), (SELECT id FROM products WHERE sku = 'AU-10002'),
        'AU-10002', 'Kamera samochodowa', 1, 349.00, 349.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0010'), (SELECT id FROM products WHERE sku = 'AU-10003'),
        'AU-10003', 'Uchwyt na telefon', 2, 39.99, 79.98),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0010'), (SELECT id FROM products WHERE sku = 'AU-10006'),
        'AU-10006', 'Apteczka samochodowa', 1, 34.99, 34.99),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0011'), (SELECT id FROM products WHERE sku = 'OF-8001'),
        'OF-8001', 'Fotel biurowy', 1, 699.00, 699.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0011'), (SELECT id FROM products WHERE sku = 'OF-8004'),
        'OF-8004', 'Notes A5', 5, 14.99, 74.95),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0011'), (SELECT id FROM products WHERE sku = 'OF-8006'),
        'OF-8006', 'Lampka biurkowa LED', 2, 129.99, 259.98),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0012'), (SELECT id FROM products WHERE sku = 'HK-3001'),
        'HK-3001', 'Ekspres do kawy DeLonghi', 1, 1299.00, 1299.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0012'), (SELECT id FROM products WHERE sku = 'BK-2006'),
        'BK-2006', 'Spring Boot w praktyce', 1, 99.99, 99.99),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0013'), (SELECT id FROM products WHERE sku = 'SP-4002'),
        'SP-4002', 'Mata do jogi', 2, 79.99, 159.98),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0013'), (SELECT id FROM products WHERE sku = 'SP-4003'),
        'SP-4003', 'Hantle 2x10 kg', 1, 199.00, 199.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0013'), (SELECT id FROM products WHERE sku = 'SP-4005'),
        'SP-4005', 'Buty biegowe Nike', 1, 449.00, 449.00),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0014'), (SELECT id FROM products WHERE sku = 'EL-1002'),
        'EL-1002', 'Smartfon Samsung Galaxy A55', 1, 1799.00, 1799.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0014'), (SELECT id FROM products WHERE sku = 'EL-1004'),
        'EL-1004', 'Monitor LG 27 cali', 2, 899.00, 1798.00),

       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0015'), (SELECT id FROM products WHERE sku = 'FA-6003'),
        'FA-6003', 'Sneakersy unisex', 1, 249.00, 249.00),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0015'), (SELECT id FROM products WHERE sku = 'TO-7004'),
        'TO-7004', 'Pluszowy miś', 1, 69.99, 69.99),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0015'), (SELECT id FROM products WHERE sku = 'TO-7006'),
        'TO-7006', 'Zestaw kreatywny', 2, 39.99, 79.98);

-- =========================
-- PAYMENTS
-- =========================

INSERT INTO payments
(order_id, provider, status, amount, external_payment_id, failure_reason, created_at, updated_at)
VALUES ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0001'), 'STRIPE', 'PAID', 609.97, 'pay_20260001', NULL,
        '2026-06-02 10:01:00', '2026-06-02 10:03:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0002'), 'PAYU', 'PAID', 484.76, 'pay_20260002', NULL,
        '2026-06-03 11:01:00', '2026-06-03 11:04:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0003'), 'STRIPE', 'PAID', 538.96, 'pay_20260003', NULL,
        '2026-06-04 09:31:00', '2026-06-04 09:35:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0004'), 'BLIK', 'PAID', 4197.00, 'pay_20260004', NULL,
        '2026-06-05 13:01:00', '2026-06-05 13:05:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0005'), 'PAYU', 'PAID', 539.96, 'pay_20260005', NULL,
        '2026-06-06 14:01:00', '2026-06-06 14:05:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0006'), 'BLIK', 'CANCELLED', 2298.98, NULL,
        'Klient anulował zamówienie przed płatnością.', '2026-06-07 17:01:00', '2026-06-07 17:30:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0007'), 'STRIPE', 'PENDING', 418.97, NULL, NULL,
        '2026-06-08 08:31:00', '2026-06-08 08:45:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0008'), 'PAYU', 'PAID', 1098.94, 'pay_20260008', NULL,
        '2026-06-09 12:01:00', '2026-06-09 12:05:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0009'), 'STRIPE', 'PAID', 339.97, 'pay_20260009', NULL,
        '2026-06-10 16:01:00', '2026-06-10 16:03:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0010'), 'BLIK', 'FAILED', 463.97, NULL,
        'Przekroczono czas potwierdzenia transakcji.', '2026-06-11 10:31:00', '2026-06-11 10:40:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0011'), 'PAYU', 'PAID', 1033.93, 'pay_20260011', NULL,
        '2026-06-12 09:01:00', '2026-06-12 09:04:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0012'), 'STRIPE', 'PAID', 1398.99, 'pay_20260012', NULL,
        '2026-06-13 09:11:00', '2026-06-13 09:14:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0013'), 'PAYU', 'PENDING', 807.98, NULL, NULL,
        '2026-06-14 13:01:00', '2026-06-14 13:15:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0014'), 'STRIPE', 'PAID', 3597.00, 'pay_20260014', NULL,
        '2026-06-15 15:21:00', '2026-06-15 15:24:00'),
       ((SELECT id FROM orders WHERE order_number = 'ORD-2026-0015'), 'BLIK', 'REFUNDED', 398.97, 'pay_20260015',
        'Zwrot po anulowaniu zamówienia przez klienta.', '2026-06-16 18:01:00', '2026-06-16 19:10:00');

-- =========================
-- STOCK MOVEMENTS
-- =========================

INSERT INTO stock_movements
(product_id, product_sku, type, quantity, reason, order_number, created_at)
VALUES ((SELECT id FROM products WHERE sku = 'EL-1001'), 'EL-1001', 'INBOUND', 20, 'Dostawa od dystrybutora.', NULL,
        '2026-05-20 09:00:00'),
       ((SELECT id FROM products WHERE sku = 'EL-1002'), 'EL-1002', 'INBOUND', 30, 'Dostawa od dystrybutora.', NULL,
        '2026-05-20 09:05:00'),
       ((SELECT id FROM products WHERE sku = 'EL-1003'), 'EL-1003', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0001', '2026-06-02 10:05:00'),
       ((SELECT id FROM products WHERE sku = 'BK-2001'), 'BK-2001', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0001', '2026-06-02 10:05:00'),

       ((SELECT id FROM products WHERE sku = 'HK-3003'), 'HK-3003', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0002', '2026-06-03 11:05:00'),
       ((SELECT id FROM products WHERE sku = 'HK-3004'), 'HK-3004', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0002', '2026-06-03 11:05:00'),
       ((SELECT id FROM products WHERE sku = 'OF-8003'), 'OF-8003', 'SOLD', -3, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0002', '2026-06-03 11:05:00'),

       ((SELECT id FROM products WHERE sku = 'FA-6002'), 'FA-6002', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0003', '2026-06-04 09:40:00'),
       ((SELECT id FROM products WHERE sku = 'FA-6004'), 'FA-6004', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0003', '2026-06-04 09:40:00'),
       ((SELECT id FROM products WHERE sku = 'FA-6006'), 'FA-6006', 'SOLD', -3, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0003', '2026-06-04 09:40:00'),

       ((SELECT id FROM products WHERE sku = 'EL-1001'), 'EL-1001', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0004', '2026-06-05 13:10:00'),
       ((SELECT id FROM products WHERE sku = 'EL-1005'), 'EL-1005', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0004', '2026-06-05 13:10:00'),
       ((SELECT id FROM products WHERE sku = 'EL-1006'), 'EL-1006', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0004', '2026-06-05 13:10:00'),

       ((SELECT id FROM products WHERE sku = 'TO-7001'), 'TO-7001', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0005', '2026-06-06 14:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7002'), 'TO-7002', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0005', '2026-06-06 14:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7003'), 'TO-7003', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0005', '2026-06-06 14:10:00'),

       ((SELECT id FROM products WHERE sku = 'SP-4001'), 'SP-4001', 'RESERVED', 1,
        'Rezerwacja produktu w anulowanym zamówieniu.', 'ORD-2026-0006', '2026-06-07 17:05:00'),
       ((SELECT id FROM products WHERE sku = 'SP-4006'), 'SP-4006', 'RESERVED', 2,
        'Rezerwacja produktu w anulowanym zamówieniu.', 'ORD-2026-0006', '2026-06-07 17:05:00'),
       ((SELECT id FROM products WHERE sku = 'SP-4001'), 'SP-4001', 'RELEASED', 1,
        'Zwolnienie rezerwacji po anulowaniu zamówienia.', 'ORD-2026-0006', '2026-06-07 17:30:00'),
       ((SELECT id FROM products WHERE sku = 'SP-4006'), 'SP-4006', 'RELEASED', 2,
        'Zwolnienie rezerwacji po anulowaniu zamówienia.', 'ORD-2026-0006', '2026-06-07 17:30:00'),

       ((SELECT id FROM products WHERE sku = 'BZ-5001'), 'BZ-5001', 'RESERVED', 1,
        'Rezerwacja produktu dla nieopłaconego zamówienia.', 'ORD-2026-0007', '2026-06-08 08:35:00'),
       ((SELECT id FROM products WHERE sku = 'BZ-5003'), 'BZ-5003', 'RESERVED', 2,
        'Rezerwacja produktu dla nieopłaconego zamówienia.', 'ORD-2026-0007', '2026-06-08 08:35:00'),
       ((SELECT id FROM products WHERE sku = 'BZ-5006'), 'BZ-5006', 'RESERVED', 1,
        'Rezerwacja produktu dla nieopłaconego zamówienia.', 'ORD-2026-0007', '2026-06-08 08:35:00'),

       ((SELECT id FROM products WHERE sku = 'GD-9003'), 'GD-9003', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0008', '2026-06-09 12:10:00'),
       ((SELECT id FROM products WHERE sku = 'GD-9005'), 'GD-9005', 'SOLD', -4, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0008', '2026-06-09 12:10:00'),
       ((SELECT id FROM products WHERE sku = 'GD-9006'), 'GD-9006', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0008', '2026-06-09 12:10:00'),

       ((SELECT id FROM products WHERE sku = 'BK-2002'), 'BK-2002', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0009', '2026-06-10 16:05:00'),
       ((SELECT id FROM products WHERE sku = 'BK-2003'), 'BK-2003', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0009', '2026-06-10 16:05:00'),
       ((SELECT id FROM products WHERE sku = 'BK-2005'), 'BK-2005', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0009', '2026-06-10 16:05:00'),

       ((SELECT id FROM products WHERE sku = 'AU-10002'), 'AU-10002', 'RESERVED', 1,
        'Rezerwacja produktu przy nieudanej płatności.', 'ORD-2026-0010', '2026-06-11 10:35:00'),
       ((SELECT id FROM products WHERE sku = 'AU-10003'), 'AU-10003', 'RESERVED', 2,
        'Rezerwacja produktu przy nieudanej płatności.', 'ORD-2026-0010', '2026-06-11 10:35:00'),
       ((SELECT id FROM products WHERE sku = 'AU-10006'), 'AU-10006', 'RESERVED', 1,
        'Rezerwacja produktu przy nieudanej płatności.', 'ORD-2026-0010', '2026-06-11 10:35:00'),

       ((SELECT id FROM products WHERE sku = 'OF-8001'), 'OF-8001', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0011', '2026-06-12 09:10:00'),
       ((SELECT id FROM products WHERE sku = 'OF-8004'), 'OF-8004', 'SOLD', -5, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0011', '2026-06-12 09:10:00'),
       ((SELECT id FROM products WHERE sku = 'OF-8006'), 'OF-8006', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0011', '2026-06-12 09:10:00'),

       ((SELECT id FROM products WHERE sku = 'HK-3001'), 'HK-3001', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0012', '2026-06-13 09:20:00'),
       ((SELECT id FROM products WHERE sku = 'BK-2006'), 'BK-2006', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0012', '2026-06-13 09:20:00'),

       ((SELECT id FROM products WHERE sku = 'SP-4002'), 'SP-4002', 'RESERVED', 2,
        'Rezerwacja produktu dla oczekującej płatności.', 'ORD-2026-0013', '2026-06-14 13:10:00'),
       ((SELECT id FROM products WHERE sku = 'SP-4003'), 'SP-4003', 'RESERVED', 1,
        'Rezerwacja produktu dla oczekującej płatności.', 'ORD-2026-0013', '2026-06-14 13:10:00'),
       ((SELECT id FROM products WHERE sku = 'SP-4005'), 'SP-4005', 'RESERVED', 1,
        'Rezerwacja produktu dla oczekującej płatności.', 'ORD-2026-0013', '2026-06-14 13:10:00'),

       ((SELECT id FROM products WHERE sku = 'EL-1002'), 'EL-1002', 'SOLD', -1, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0014', '2026-06-15 15:30:00'),
       ((SELECT id FROM products WHERE sku = 'EL-1004'), 'EL-1004', 'SOLD', -2, 'Sprzedaż produktu w zamówieniu.',
        'ORD-2026-0014', '2026-06-15 15:30:00'),

       ((SELECT id FROM products WHERE sku = 'FA-6003'), 'FA-6003', 'SOLD', -1,
        'Sprzedaż produktu w zamówieniu anulowanym po płatności.', 'ORD-2026-0015', '2026-06-16 18:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7004'), 'TO-7004', 'SOLD', -1,
        'Sprzedaż produktu w zamówieniu anulowanym po płatności.', 'ORD-2026-0015', '2026-06-16 18:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7006'), 'TO-7006', 'SOLD', -2,
        'Sprzedaż produktu w zamówieniu anulowanym po płatności.', 'ORD-2026-0015', '2026-06-16 18:10:00'),

       ((SELECT id FROM products WHERE sku = 'FA-6003'), 'FA-6003', 'RETURNED', 1,
        'Zwrot produktu po anulowaniu zamówienia.', 'ORD-2026-0015', '2026-06-16 19:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7004'), 'TO-7004', 'RETURNED', 1,
        'Zwrot produktu po anulowaniu zamówienia.', 'ORD-2026-0015', '2026-06-16 19:10:00'),
       ((SELECT id FROM products WHERE sku = 'TO-7006'), 'TO-7006', 'RETURNED', 2,
        'Zwrot produktu po anulowaniu zamówienia.', 'ORD-2026-0015', '2026-06-16 19:10:00');

SELECT setval(
               pg_get_serial_sequence('categories', 'id'),
               COALESCE((SELECT MAX(id) FROM categories), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('products', 'id'),
               COALESCE((SELECT MAX(id) FROM products), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('users', 'id'),
               COALESCE((SELECT MAX(id) FROM users), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('carts', 'id'),
               COALESCE((SELECT MAX(id) FROM carts), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('cart_items', 'id'),
               COALESCE((SELECT MAX(id) FROM cart_items), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('orders', 'id'),
               COALESCE((SELECT MAX(id) FROM orders), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('order_items', 'id'),
               COALESCE((SELECT MAX(id) FROM order_items), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('payments', 'id'),
               COALESCE((SELECT MAX(id) FROM payments), 0) + 1,
               false
       );

SELECT setval(
               pg_get_serial_sequence('stock_movements', 'id'),
               COALESCE((SELECT MAX(id) FROM stock_movements), 0) + 1,
               false
       );