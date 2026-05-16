CREATE TABLE IF NOT EXISTS accesss (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT,
    gate VARCHAR(255),
    access_time VARCHAR(255),
    status VARCHAR(50)
);
