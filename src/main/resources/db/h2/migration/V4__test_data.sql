-- =============================================
-- TEST FIXTURES
-- =============================================

-- Wedding A (couple user's wedding)
INSERT INTO weddings (id, couple_name, slug, date, invite_message, gift_message, created_at, updated_at, is_active)
VALUES ('11111111-1111-1111-1111-111111111111', 'Alice & Bob', 'alice-bob', '2026-12-20 18:00:00', 'You are invited!', 'Check our gifts', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Wedding B (another wedding, for tenant isolation tests)
INSERT INTO weddings (id, couple_name, slug, date, invite_message, gift_message, created_at, updated_at, is_active)
VALUES ('22222222-2222-2222-2222-222222222222', 'Carol & Dave', 'carol-dave', '2026-11-15 17:00:00', 'Join us!', 'Gift list here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Admin user (no specific wedding scope, linked to wedding A for FK)
INSERT INTO users (id, auth_user_id, email, name, role, wedding_id, created_at, updated_at, is_active)
VALUES ('aaaa0000-0000-0000-0000-000000000001', 'aaaa0000-0000-0000-0000-aaaaaaaaaaaa', 'admin@hestia.com', 'Admin User', 'ADMIN', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Couple user (owns wedding A)
INSERT INTO users (id, auth_user_id, email, name, role, wedding_id, created_at, updated_at, is_active)
VALUES ('bbbb0000-0000-0000-0000-000000000001', 'bbbb0000-0000-0000-0000-bbbbbbbbbbbb', 'couple@hestia.com', 'Couple User', 'COUPLE', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Invites for wedding A
INSERT INTO invites (id, name, phone, wedding_id, created_at, updated_at, is_active)
VALUES ('cccc0000-0000-0000-0000-000000000001', 'Familia Silva', '11999990001', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Invites for wedding B
INSERT INTO invites (id, name, phone, wedding_id, created_at, updated_at, is_active)
VALUES ('cccc0000-0000-0000-0000-000000000002', 'Familia Santos', '11999990002', '22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Guests for wedding A
INSERT INTO guests (id, name, age_group, status, wedding_id, invite_id, created_at, updated_at, is_active)
VALUES ('dddd0000-0000-0000-0000-000000000001', 'João Silva', 'ADULT', 'CONFIRMED', '11111111-1111-1111-1111-111111111111', 'cccc0000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Guests for wedding B
INSERT INTO guests (id, name, age_group, status, wedding_id, invite_id, created_at, updated_at, is_active)
VALUES ('dddd0000-0000-0000-0000-000000000002', 'Maria Santos', 'ADULT', 'PENDING', '22222222-2222-2222-2222-222222222222', 'cccc0000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Gifts for wedding A
INSERT INTO gifts (id, description, price, stock, wedding_id, created_at, updated_at, is_active)
VALUES ('eeee0000-0000-0000-0000-000000000001', 'Jogo de Panelas', 25000, 2, '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Gifts for wedding B
INSERT INTO gifts (id, description, price, stock, wedding_id, created_at, updated_at, is_active)
VALUES ('eeee0000-0000-0000-0000-000000000002', 'Liquidificador', 15000, 1, '22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Messages for wedding A
INSERT INTO messages (id, sender, message, wedding_id, created_at, updated_at, is_active)
VALUES ('ffff0000-0000-0000-0000-000000000001', 'Tia Maria', 'Parabéns pelo casamento!', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Messages for wedding B
INSERT INTO messages (id, sender, message, wedding_id, created_at, updated_at, is_active)
VALUES ('ffff0000-0000-0000-0000-000000000002', 'Vovó Ana', 'Felicidades!', '22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Order for webhook tests (PENDING, linked to gift A, wedding A)
INSERT INTO orders (id, guest_name, guest_email, amount, status, payment_id, wedding_id, gift_id, created_at, updated_at, is_active)
VALUES ('bbbb1111-0000-0000-0000-000000000001', 'Test Guest', 'guest@test.com', 25000, 'PENDING', 'cccc1111-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'eeee0000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Wallet for wedding A (balance crediting in webhook tests)
INSERT INTO wallets (id, pix_key, balance, fee, is_active, created_at, updated_at, wedding_id)
VALUES ('11111111-1111-1111-1111-111111111111', 'test@pix.com', 0, 500, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '11111111-1111-1111-1111-111111111111');

-- Set wallet balance to 50000 (500 BRL) so transfer tests have funds
UPDATE wallets SET balance = 50000 WHERE id = '11111111-1111-1111-1111-111111111111';
