-- Enum for user role
CREATE TYPE user_role_enum AS ENUM (
    'COUPLE',
    'ADMIN'
);

-- Wedding table creation
CREATE TABLE weddings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    couple_name text NOT NULL,
    date date,
    picture text,
    invite_message text,
    gift_message text,
    slug text NOT NULL UNIQUE,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    is_active boolean NOT NULL DEFAULT true
);

-- Trigger to auto-update updated_at
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
    wedding_id uuid,
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
