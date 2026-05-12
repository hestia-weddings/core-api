-- Create Invite with Guests RPC

CREATE OR REPLACE FUNCTION create_invite_with_guests(
    p_guests jsonb,
    p_name text,
    p_phone text,
    p_wedding_id uuid
)
RETURNS invites
LANGUAGE plpgsql
AS $$
DECLARE
    new_invite invites;
BEGIN
    INSERT INTO invites (name, phone, wedding_id)
    VALUES (p_name, p_phone, p_wedding_id)
    RETURNING * INTO new_invite;

    IF p_guests IS NOT NULL AND jsonb_array_length(p_guests) > 0 THEN
        INSERT INTO guests (name, age_group, status, invite_id, wedding_id)
        SELECT
            guest->>'name',
            (guest->>'age_group')::age_group_enum,
            'PENDING'::guest_status_enum,
            new_invite.id,
            p_wedding_id
        FROM jsonb_array_elements(p_guests) AS guest;
    END IF;

    RETURN new_invite;
END;
$$;
