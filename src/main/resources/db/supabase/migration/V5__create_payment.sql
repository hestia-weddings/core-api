-- Enum for order status
CREATE TYPE order_status_enum AS ENUM (
    'PENDING',
    'PAID',
    'FAILED',
    'EXPIRED'
);

-- Enum for payment environment
CREATE TYPE payment_env_enum AS ENUM (
    'PRODUCTION',
    'SANDBOX'
)

-- Orders table creation
CREATE TABLE orders (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    guest_name text NOT NULL,
    guest_email text NOT NULL,
    amount int NOT NULL,
    status order_status_enum NOT NULL DEFAULT 'PENDING',
    payment_id uuid NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    gift_id uuid NOT NULL,
    CONSTRAINT fk_orders_gift
        FOREIGN KEY (gift_id)
            REFERENCES gifts (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_orders_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
);

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_orders
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Payment Configs table creation
CREATE TABLE payment_configs (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    api_key text NOT NULL,
    environment payment_env_enum NOT NULL DEFAULT 'SANDBOX',
    webhook_token varchar(64) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL UNIQUE,
    CONSTRAINT fk_payment_config_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
)

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_payment_configs
    BEFORE UPDATE ON payment_configs
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
