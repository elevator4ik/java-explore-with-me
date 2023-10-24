CREATE TABLE IF NOT EXISTS stats
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    income_date TIMESTAMP WITHOUT TIME ZONE,
    uri         VARCHAR(1000),
    ip          VARCHAR(15),
    app         VARCHAR(200)
);
