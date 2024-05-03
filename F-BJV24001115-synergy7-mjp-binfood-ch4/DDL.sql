CREATE TABLE tbl_users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username            VARCHAR(32) NOT NULL UNIQUE,
    email               VARCHAR(256) NOT NULL UNIQUE,
    password            VARCHAR(60) NOT NULL,
    access_token        VARCHAR(128),
    token_expired_at    BIGINT
);

CREATE TABLE tbl_merchants (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255),
    location    VARCHAR(255),
    open        BOOLEAN
);

CREATE TABLE tbl_orders (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_time  TIMESTAMP,
    destination VARCHAR(255),
    user_id     UUID,
    completed   BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES tbl_users (id)
);

CREATE TABLE tbl_products (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255),
    price       BIGINT,
    merchant_id UUID,
    FOREIGN KEY (merchant_id) REFERENCES tbl_merchants (id)
);

CREATE TABLE tbl_order_details (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID,
    product_id  UUID,
    quantity    INT,
    total_price BIGINT,
    FOREIGN KEY (order_id) REFERENCES tbl_orders (id),
    FOREIGN KEY (product_id) REFERENCES tbl_products (id)
);
