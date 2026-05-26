-- Enum for guest age group
CREATE TYPE age_group_enum AS ENUM (
    'BABY',
    'CHILD',
    'ADULT'
);

-- Enum for guest status
CREATE TYPE guest_status_enum AS ENUM (
    'CONFIRMED',
    'DECLINED',
    'PENDING'
);

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

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_messages
    BEFORE UPDATE ON messages
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

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_gifts
    BEFORE UPDATE ON gifts
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

-- Trigger to auto-update updated_at
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

-- Trigger to auto-update updated_at
CREATE TRIGGER set_updated_at_guests
    BEFORE UPDATE ON guests
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
