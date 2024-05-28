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
    RETURNS TABLE(id UUID, name VARCHAR, location VARCHAR, open BOOLEAN, created_at TIMESTAMP, updated_at TIMESTAMP)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_merchants as m
    SET name = new_name,
        location = new_location,
        updated_at = NOW()
    WHERE m.id = merchant_id;

    RETURN QUERY
        SELECT
            m.id, m.name, m.location, m.open, m.created_at, m.updated_at
        FROM tbl_merchants AS m
        WHERE m.id = merchant_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Merchant with ID % not found', merchant_id;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION update_product_info(
    product_id UUID,
    new_name VARCHAR(255),
    new_price BIGINT
)
    RETURNS TABLE(id UUID, name VARCHAR, price BIGINT, created_at TIMESTAMP, updated_at TIMESTAMP)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE tbl_products as p
    SET name = new_name,
        price = new_price,
        updated_at = NOW()
    WHERE p.id = product_id;

    RETURN QUERY
        SELECT
            p.id, p.name, p.price, p.created_at, p.updated_at
        FROM tbl_products AS p
        WHERE p.id = product_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Product with ID % not found', product_id;
    END IF;
END;
$$;
