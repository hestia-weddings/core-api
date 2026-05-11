-- Extension to handle UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ENUMs to be used after
CREATE TYPE age_group_enum AS ENUM (
    'BABY',
    'CHILD',
    'ADULT'
);

CREATE TYPE guest_status_enum AS ENUM (
    'CONFIRMED',
    'DECLINED',
    'PENDING'
);

CREATE TYPE order_status_enum AS ENUM (
    'PENDING',
    'PAID'
);

CREATE TYPE user_role_enum AS ENUM (
    'COUPLE',
    'ADMIN'
);

-- Trigger to auto update updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$ LANGUAGE plpgsql;
