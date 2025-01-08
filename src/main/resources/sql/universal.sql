CREATE TABLE IF NOT EXISTS `universal` (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               card_name VARCHAR(255) NOT NULL,
                                               is_reversed BOOLEAN NOT NULL,
                                               img_url VARCHAR(255),
                                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
