CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    event_id BIGINT,
    seat_id BIGINT,
    price DOUBLE,
    status VARCHAR(50)
);
