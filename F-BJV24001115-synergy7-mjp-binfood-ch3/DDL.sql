CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE merchants (
    id UUID PRIMARY KEY,
    merchant_name VARCHAR(255) NOT NULL,
    merchant_location VARCHAR(255),
    open BOOLEAN NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    merchant_id UUID REFERENCES merchants(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    order_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    destination_address VARCHAR(255),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    completed BOOLEAN NOT NULL
);

CREATE TABLE order_details (
    id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    totalPrice INTEGER NOT NULL CHECK (totalPrice >= 0)
);
