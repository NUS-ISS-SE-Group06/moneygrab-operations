USE reservationdb;


INSERT INTO reservation (
    customer_id, money_changer_id, currency_id, exchange_rate, foreign_amount, sgd_amount,
    status, expires_at, created_at, updated_at, created_by, updated_by
) VALUES
-- 1. USD reservation
(1, 1, 1, 1.35000000, 500.00, 675.00, 'PENDING', '2025-07-08 12:00:00', NOW(), NOW(), 9001, 9001),

-- 2. EUR reservation
(2, 2, 2, 1.48000000, 300.00, 444.00, 'CONFIRMED', '2025-07-08 14:30:00', NOW(), NOW(), 9002, 9002),

-- 3. IDR reservation with unit 1000
(3, 1, 3, 0.00009000, 1000000.00, 90.00, 'EXPIRED', '2025-07-08 09:00:00', NOW(), NOW(), 9003, 9003),

-- 4. TWD reservation with unit 100
(4, 3, 4, 0.04300000, 20000.00, 860.00, 'CANCELLED', '2025-07-08 08:00:00', NOW(), NOW(), 9004, 9004),

-- 5. VND reservation with unit 100000
(5, 2, 5, 0.00005400, 5000000.00, 270.00, 'PENDING', '2025-07-08 16:00:00', NOW(), NOW(), 9005, 9005);




INSERT INTO transaction (
    reservation_id, transaction_date, customer_id, current_status, email, comments,
    money_changer_id, currency_id, exchange_rate, foreign_amount, sgd_amount,
    received_cash, created_at, updated_at, created_by, updated_by
) VALUES
-- Transaction 1: Linked to confirmed EUR reservation (id = 2)
(2, '2025-07-08 14:35:00', 102, 'CONFIRMED', 'jane.doe@example.com', 'Customer showed email confirmation.',
 2, 2, 1.48000000, 300.00, 444.00, 450.00, NOW(), NOW(), 9002, 9002),

-- Transaction 2: Walk-in USD transaction (no reservation_id)
(NULL, '2025-07-08 15:00:00', 106, 'CONFIRMED', 'john.walkin@example.com', 'Walk-in customer, no prior reservation.',
 1, 1, 1.34500000, 700.00, 941.50, 950.00, NOW(), NOW(), 9001, 9001);


