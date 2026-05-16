CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    venue_id BIGINT,
    date VARCHAR(255),
    status VARCHAR(50)
);
