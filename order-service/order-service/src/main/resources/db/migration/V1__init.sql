CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    total_amount DOUBLE,
    order_date VARCHAR(255),
    status VARCHAR(50)
);
