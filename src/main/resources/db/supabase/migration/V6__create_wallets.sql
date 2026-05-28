DROP TRIGGER IF EXISTS set_updated_at_payment_configs ON payment_configs;
DROP TABLE IF EXISTS payment_configs;
DROP TYPE IF EXISTS payment_env_enum;

CREATE TABLE wallets (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    pix_key varchar(255),
    balance integer NOT NULL DEFAULT 0,
    fee integer NOT NULL DEFAULT 500,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    wedding_id uuid NOT NULL UNIQUE,
    CONSTRAINT fk_wallet_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at_wallets
    BEFORE UPDATE ON wallets
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
