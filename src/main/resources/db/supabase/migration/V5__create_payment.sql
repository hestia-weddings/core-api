-- Enum for order status
CREATE TYPE order_status_enum AS ENUM (
    'PENDING',
    'PAID',
    'FAILED',
    'EXPIRED'
);

-- Enum for transaction status
CREATE TYPE transaction_status_enum AS ENUM (
    'PENDING',
    'COMPLETED',
    'FAILED'
);

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
    message_id uuid UNIQUE,
    wedding_id uuid NOT NULL,
    gift_id uuid NOT NULL,
    CONSTRAINT fk_orders_gift
        FOREIGN KEY (gift_id)
            REFERENCES gifts (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_orders_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_orders_message
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE RESTRICT
);

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_orders
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Wallets table creation
CREATE TABLE wallets (
     id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
     pix_key VARCHAR(255),
     balance INTEGER NOT NULL DEFAULT 0,
     fee INTEGER NOT NULL DEFAULT 500,
     is_active BOOLEAN NOT NULL DEFAULT TRUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     wedding_id UUID NOT NULL UNIQUE,
     CONSTRAINT fk_wallet_wedding
         FOREIGN KEY (wedding_id)
             REFERENCES weddings(id)
             ON DELETE CASCADE
);

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_wallets
    BEFORE UPDATE ON wallets
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Transactions table creation
CREATE TABLE transactions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    amount INTEGER NOT NULL,
    fee INTEGER NOT NULL,
    status transaction_status_enum NOT NULL DEFAULT 'PENDING',
    asaas_transfer_id VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    wallet_id UUID NOT NULL,
    wedding_id UUID NOT NULL,
    CONSTRAINT fk_transaction_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_transaction_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings(id)
            ON DELETE CASCADE
);

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_transactions
    BEFORE UPDATE ON transactions
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
