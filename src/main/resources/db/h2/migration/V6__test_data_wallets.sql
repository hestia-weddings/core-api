-- Wallet for wedding A (balance crediting in webhook tests)
INSERT INTO wallets (id, pix_key, balance, fee, is_active, created_at, updated_at, wedding_id)
VALUES ('11111111-1111-1111-1111-111111111111', 'test@pix.com', 0, 500, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '11111111-1111-1111-1111-111111111111');
