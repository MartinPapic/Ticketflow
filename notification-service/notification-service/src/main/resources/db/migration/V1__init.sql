CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    message TEXT,
    type VARCHAR(50),
    sent_at VARCHAR(255),
    status VARCHAR(50)
);
