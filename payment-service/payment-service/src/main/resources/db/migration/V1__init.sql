CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    amount DOUBLE,
    payment_method VARCHAR(255),
    payment_date VARCHAR(255),
    status VARCHAR(50)
);
