-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE IF NOT EXISTS users
(
    id VARCHAR(80),
    first_name    VARCHAR(256),
    second_name   VARCHAR(256),
    age     INT,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS location
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(80),
    city VARCHAR(128),
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS tags
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    tag_type ENUM('bio'),
    tag_value TEXT,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS user_tags
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    tag_id BIGINT,
    user_id VARCHAR(80),
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS user_credentials
(
    user_id VARCHAR(80),
    bcrypt BINARY(60),
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (user_id)
);

--changeset liquibase:exporter
CREATE USER IF NOT EXISTS 'exporter'@'localhost' IDENTIFIED BY 'exporter';
GRANT PROCESS, REPLICATION CLIENT ON *.* TO 'exporter'@'localhost';
GRANT SELECT ON performance_schema.* TO 'exporter'@'localhost';

--changeset liquibase:synthetictable
CREATE TABLE IF NOT EXISTS numbers
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

--changeset liquibase:missingindex
CREATE INDEX name_surname_index ON users(first_name, second_name);

--changeset liquibase:addToken
ALTER TABLE user_credentials ADD COLUMN token VARCHAR(60)

--changeset liquibase:posts
CREATE TABLE IF NOT EXISTS posts
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    user_id    VARCHAR(80),
    post_title TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS posts_content
(
    post_id BIGINT NOT NULL,
    post    MEDIUMTEXT,
    FOREIGN KEY (post_id) REFERENCES posts (id)
    ON DELETE CASCADE
    );
CREATE INDEX posts_idx ON posts (id);
CREATE INDEX posts_content_idx ON posts_content (post_id);

--changeset liquibase:friends
CREATE TABLE IF NOT EXISTS friends
(
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    subscriber_id VARCHAR(80) NOT NULL,
    publisher_id  VARCHAR(80) NOT NULL,
    mutual        BOOLEAN,
    FOREIGN KEY (subscriber_id) REFERENCES users (id)
    ON DELETE CASCADE,
    FOREIGN KEY (publisher_id) REFERENCES users (id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
    );
CREATE INDEX by_subscriber_idx ON friends (subscriber_id);
CREATE INDEX by_publisher_idx ON friends (publisher_id);
