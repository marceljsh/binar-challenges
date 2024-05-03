CREATE OR REPLACE PROCEDURE update_merchant_open(
    IN merchant_id INT,
    IN new_open BOOLEAN
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE tbl_merchants
    SET open = new_open
    WHERE id = merchant_id;

    IF @@ROWCOUNT = 0 THEN
        RAISE EXCEPTION 'Merchant with ID % not found', merchant_id;
    END IF;
END;
$$;

CREATE OR REPLACE PROCEDURE update_order_completed(
    IN order_id INT,
    IN new_completed BOOLEAN
) LANGUAGE plpgsql AS $$
BEGIN
UPDATE tbl_orders
SET completed = new_completed
WHERE id = order_id;

IF @@ROWCOUNT = 0 THEN
        RAISE EXCEPTION 'Order with ID % not found', order_id;
END IF;
END;
$$;
