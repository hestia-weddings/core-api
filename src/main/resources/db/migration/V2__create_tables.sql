-- Wedding table creation
CREATE TABLE weddings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    couple_name text NOT NULL,
    date timestamptz,
    picture text,
    invite_message text,
    gift_message text,
    slug text NOT NULL UNIQUE,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true
);

CREATE TRIGGER set_updated_at_weddings
    BEFORE UPDATE ON weddings
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Users table creation
CREATE TABLE users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    auth_user_id uuid NOT NULL UNIQUE,
    email text NOT NULL UNIQUE,
    name text NOT NULL,
    role user_role_enum NOT NULL DEFAULT 'COUPLE',
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    CONSTRAINT fk_users_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings(id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_users_auth
        FOREIGN KEY (auth_user_id)
            REFERENCES auth.users(id)
            ON DELETE CASCADE
);

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_users
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Messages table creation
CREATE TABLE messages (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    sender text NOT NULL,
    message text NOT NULL,
    is_favorite boolean NOT NULL DEFAULT false,
    is_new boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    CONSTRAINT fk_messages_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at_messages
    BEFORE UPDATE ON messages
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Invites table creation
CREATE TABLE invites (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    phone text,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    CONSTRAINT fk_invites_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at_invites
    BEFORE UPDATE ON invites
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Guests table creation
CREATE TABLE guests (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    age_group age_group_enum NOT NULL,
    status guest_status_enum NOT NULL DEFAULT 'PENDING',
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    invite_id uuid NOT NULL,
    CONSTRAINT fk_guests_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_guests_invite
        FOREIGN KEY (invite_id)
            REFERENCES invites (id)
            ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at_guests
    BEFORE UPDATE ON guests
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Gifts table creation
CREATE TABLE gifts (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    description text NOT NULL,
    picture text,
    price int NOT NULL,
    stock int NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true,
    wedding_id uuid NOT NULL,
    CONSTRAINT fk_gifts_wedding
        FOREIGN KEY (wedding_id)
            REFERENCES weddings (id)
            ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at_gifts
    BEFORE UPDATE ON gifts
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Orders table creation
CREATE TABLE orders (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    purchased_by text NOT NULL,
    price int NOT NULL,
    asaas_id text NOT NULL,
    link_url text NOT NULL,
    status order_status_enum NOT NULL DEFAULT 'PENDING',
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

CREATE TRIGGER set_updated_at_orders
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
