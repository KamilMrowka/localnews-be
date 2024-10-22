CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    state_name VARCHAR(255),
    has_articles BOOLEAN DEFAULT FALSE
);

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    link VARCHAR(255) NOT NULL,
    description text NOT NULL,
    article text NOT NULL,
    global BOOLEAN DEFAULT FALSE,
    city_id BIGINT,
    FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE RESTRICT
)
