-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE IF NOT EXISTS users
(
    id      INT,
    name    VARCHAR,
    surname VARCHAR,
    age     INT,
    sex     ENUM('male', 'female', 'not_stated'),
    PRIMARY KEY (id)
)
CREATE TABLE IF NOT EXISTS location
(
    id INT,
    user_id INT,
    city VARCHAR,
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
)
CREATE TABLE IF NOT EXISTS tags
(
    id INT,
    tag_type ENUM('hobby'),
    tag_value VARCHAR,
    PRIMARY KEY (id)
)
CREATE TABLE IF NOT EXISTS user_tags
(
    id INT,
    tag_id INT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    FOREIGN KEY (tag) REFERENCES tags(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
)
CREATE TABLE IF NOT EXISTS user_credentials
(
    user_id INT,
    bcrypt BINARY(40),
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (user_id)
)