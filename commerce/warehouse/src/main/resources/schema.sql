CREATE TABLE IF NOT EXISTS warehouse_product (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN NOT NULL,
    width DECIMAL(10,2) NOT NULL,
    height DECIMAL(10,2) NOT NULL,
    depth DECIMAL(10,2) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id         UUID NOT NULL,
    delivery_id      UUID
);

CREATE TABLE IF NOT EXISTS booking_products (
    booking_id       UUID NOT NULL,
    product_id       UUID NOT NULL,
    quantity         BIGINT,
    CONSTRAINT booking_products_pk PRIMARY KEY (booking_id, product_id),
    CONSTRAINT booking_products_booking_fk FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
);