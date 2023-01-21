CREATE TABLE tmp_tbl
(

    id          VARCHAR(80),
    first_name  VARCHAR(256),
    second_name VARCHAR(256),
    age         INT,
    city VARCHAR(128),
    PRIMARY KEY (id)
);
# LOAD DATA INFILE '/tmp/ppl.csv' INTO TABLE tmp_tbl
LOAD DATA INFILE '/var/lib/mysql-files/people.csv' INTO TABLE tmp_tbl
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
ESCAPED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

WITH uid AS (INSERT INTO users (first_name, second_name, age) VALUES (first_name, second_name, age)) RETURNING id)
INSERT INTO location (user_id, city)
SELECT uid, city
FROM tmp_tbl;
