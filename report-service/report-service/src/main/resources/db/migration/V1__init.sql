CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(50),
    generated_at VARCHAR(255),
    file_url VARCHAR(255)
);
