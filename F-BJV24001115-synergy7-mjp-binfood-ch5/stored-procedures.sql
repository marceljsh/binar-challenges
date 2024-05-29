CREATE OR REPLACE PROCEDURE update_merchant_open(
    IN merchant_id UUID,
    IN new_open BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_merchants
    SET open = new_open,
        updated_at = NOW()
    WHERE id = merchant_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Merchant with ID % not found', merchant_id;
    END IF;
END;
$$;



CREATE OR REPLACE PROCEDURE update_order_completed(
    IN order_id UUID,
    IN new_completed BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_orders
    SET completed = new_completed,
        updated_at = NOW()
    WHERE id = order_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Order with ID % not found', order_id;
    END IF;
END;
$$;



CREATE OR REPLACE FUNCTION update_merchant_info(
    merchant_id UUID,
    new_name VARCHAR(255),
    new_location VARCHAR(255)
)
RETURNS TABLE(id UUID, name VARCHAR, location VARCHAR, open BOOLEAN, created_at TIMESTAMP, updated_at TIMESTAMP, deleted_at TIMESTAMP)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_merchants as m
    SET name = new_name,
        location = new_location,
        updated_at = NOW()
    WHERE m.id = merchant_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Merchant with ID % not found', merchant_id;
    END IF;

    RETURN QUERY
    SELECT m.id, m.name, m.location, m.open, m.created_at, m.updated_at, m.deleted_at
    FROM tbl_merchants AS m
    WHERE m.id = merchant_id;
END;
$$;



CREATE OR REPLACE FUNCTION update_product_info(
    product_id UUID,
    new_name VARCHAR(255),
    new_price BIGINT
)
RETURNS TABLE(id UUID, name VARCHAR, price BIGINT, seller_id UUID, created_at TIMESTAMP, updated_at TIMESTAMP, deleted_at TIMESTAMP)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_products as p
    SET name = new_name,
        price = new_price,
        updated_at = NOW()
    WHERE p.id = product_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Product with ID % not found', product_id;
    END IF;

    RETURN QUERY
    SELECT p.id, p.name, p.price, p.seller_id, p.created_at, p.updated_at, p.deleted_at
    FROM tbl_products AS p
    WHERE p.id = product_id;
END;
$$;



CREATE OR REPLACE FUNCTION update_user_info(
    user_id UUID,
    new_username VARCHAR(32)
)
RETURNS TABLE(id UUID, username VARCHAR, email VARCHAR, password VARCHAR, access_token VARCHAR, token_expired_at BIGINT, created_at TIMESTAMP, updated_at TIMESTAMP, deleted_at TIMESTAMP)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_users as u
    SET username = new_username,
        updated_at = NOW()
    WHERE u.id = user_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'User with ID % not found', user_id;
    END IF;

    RETURN QUERY
    SELECT u.id, u.username, u.email, u.password, u.access_token, u.token_expired_at, u.created_at, u.updated_at, u.deleted_at
    FROM tbl_users AS u
    WHERE u.id = user_id;
END;
$$;
