-- Extension to handle UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Trigger to auto update updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$ LANGUAGE plpgsql;
