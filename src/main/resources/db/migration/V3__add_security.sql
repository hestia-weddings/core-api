-- RLS and Constraints

-- Weddings rules
ALTER TABLE weddings ENABLE ROW LEVEL SECURITY;

CREATE POLICY "public can select wedding"
ON weddings
FOR SELECT
USING (
    is_active = true
);

CREATE POLICY "authenticated can update wedding"
ON weddings
FOR UPDATE
USING (
    auth.role() = 'authenticated'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'authenticated'
    AND is_active = true
);

ALTER TABLE weddings
    ADD CONSTRAINT weddings_couple_name_not_empty
        CHECK (length(trim(couple_name)) > 0);

-- Users rules
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

CREATE POLICY "authenticated can select own user"
ON users
FOR SELECT
USING (
    auth.role() = 'authenticated'
    AND id = auth.uid()
);

ALTER TABLE users
    ADD CONSTRAINT users_name_length
        CHECK (length(trim(name)) > 0 AND length(name) <= 50);

-- Messages rules
ALTER TABLE messages ENABLE ROW LEVEL SECURITY;

CREATE POLICY "authenticated can select messages"
ON messages
FOR SELECT
USING (
    auth.role() = 'authenticated'
    AND is_active = true
);

CREATE POLICY "guest can insert messages"
ON messages
FOR INSERT
WITH CHECK (
    auth.role() = 'anon'
    AND is_new = true
    AND is_favorite = false
    AND is_active = true
);

CREATE POLICY "authenticated can update messages"
ON messages
FOR UPDATE
USING (
    auth.role() = 'authenticated'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'authenticated'
    AND is_active = true
);

ALTER TABLE messages
    ADD CONSTRAINT messages_sender_not_empty
        CHECK (length(trim(sender)) > 0);

ALTER TABLE messages
    ADD CONSTRAINT messages_message_not_empty
        CHECK (length(trim(message)) > 0);

-- Invites rules
ALTER TABLE invites ENABLE ROW LEVEL SECURITY;

CREATE POLICY "public can select invites"
ON invites
FOR SELECT
USING (
    is_active = true
);

CREATE POLICY "authenticated can insert invites"
ON invites
FOR INSERT
WITH CHECK (
    auth.role() = 'authenticated'
    AND is_active = true
);

CREATE POLICY "authenticated can update invites"
ON invites
FOR UPDATE
USING (
    auth.role() = 'authenticated'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'authenticated'
);

ALTER TABLE invites
    ADD CONSTRAINT invites_name_not_empty
        CHECK (length(trim(name)) > 0);

-- Guests rules
ALTER TABLE guests ENABLE ROW LEVEL SECURITY;

CREATE POLICY "public can select guests"
ON guests
FOR SELECT
USING (
    is_active = true
);

CREATE POLICY "authenticated can insert guests"
ON guests
FOR INSERT
WITH CHECK (
    auth.role() = 'authenticated'
    AND status = 'PENDING'
    AND is_active = true
);

CREATE POLICY "guest can rsvp guest"
ON guests
FOR UPDATE
USING (
    auth.role() = 'anon'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'anon'
    AND status IN ('CONFIRMED', 'DECLINED')
    AND is_active = true
);

CREATE POLICY "authenticated can update guests"
ON guests
FOR UPDATE
USING (
    auth.role() = 'authenticated'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'authenticated'
);

ALTER TABLE guests
    ADD CONSTRAINT guests_name_not_empty
        CHECK (length(trim(name)) > 0);

-- Gifts rules
ALTER TABLE gifts ENABLE ROW LEVEL SECURITY;

CREATE POLICY "public can view active gifts"
ON gifts
FOR SELECT
USING (
    is_active = true
);

CREATE POLICY "authenticated can insert gifts"
ON gifts
FOR INSERT
WITH CHECK (
    auth.role() = 'authenticated'
    AND is_active = true
);

CREATE POLICY "authenticated can update gifts"
ON gifts
FOR UPDATE
USING (
    auth.role() = 'authenticated'
    AND is_active = true
)
WITH CHECK (
    auth.role() = 'authenticated'
);

ALTER TABLE gifts
    ADD CONSTRAINT gifts_stock_positive
        CHECK (stock > 0);

ALTER TABLE gifts
    ADD CONSTRAINT gifts_price_positive
        CHECK (price >= 100);

-- Orders rules
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;

CREATE POLICY "authenticated can select orders"
ON orders
FOR SELECT
USING (
    auth.role() = 'authenticated'
);

CREATE POLICY "guest can insert orders"
ON orders
FOR INSERT
WITH CHECK (
    auth.role() = 'anon'
    AND status = 'PENDING'
    AND gift_id IS NOT NULL
    AND is_active = true
);

ALTER TABLE orders
    ADD CONSTRAINT orders_price_positive
        CHECK (price >= 100);

ALTER TABLE orders
    ADD CONSTRAINT orders_always_active
        CHECK (is_active = true);
