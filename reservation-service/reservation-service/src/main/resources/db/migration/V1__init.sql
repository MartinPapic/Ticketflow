CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    event_id BIGINT,
    seat_id BIGINT,
    status VARCHAR(50),
    expiration_time VARCHAR(255)
);
