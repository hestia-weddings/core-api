CREATE TABLE weddings (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    couple_name VARCHAR NOT NULL,
    date DATE,
    picture VARCHAR,
    invite_message VARCHAR,
    gift_message VARCHAR,
    slug VARCHAR NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE users (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    auth_user_id UUID NOT NULL UNIQUE,
    email VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    role user_role_enum NOT NULL DEFAULT 'COUPLE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    CONSTRAINT fk_users_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE RESTRICT,
    CONSTRAINT chk_user_role CHECK (role IN ('COUPLE', 'ADMIN'))
);

CREATE TABLE messages (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    sender VARCHAR NOT NULL,
    message VARCHAR NOT NULL,
    is_favorite BOOLEAN NOT NULL DEFAULT FALSE,
    is_new BOOLEAN NOT NULL DEFAULT TRUE,
    type message_type_enum NOT NULL DEFAULT 'GENERAL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    CONSTRAINT fk_messages_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE,
    CONSTRAINT chk_message_type CHECK (type IN ('GIFT', 'RSVP', 'GENERAL'))
);

CREATE TABLE invites (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR NOT NULL,
    phone VARCHAR,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    message_id UUID,
    CONSTRAINT fk_invites_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE,
    CONSTRAINT fk_invites_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE RESTRICT,
    CONSTRAINT uq_invites_message UNIQUE (message_id)
);

CREATE TABLE guests (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR NOT NULL,
    age_group age_group_enum NOT NULL,
    status guest_status_enum NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    invite_id UUID NOT NULL,
    CONSTRAINT fk_guests_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE,
    CONSTRAINT fk_guests_invite FOREIGN KEY (invite_id) REFERENCES invites(id) ON DELETE CASCADE,
    CONSTRAINT chk_age_group CHECK (age_group IN ('BABY', 'CHILD', 'ADULT')),
    CONSTRAINT chk_guest_status CHECK (status IN ('CONFIRMED', 'DECLINED', 'PENDING'))
);

CREATE TABLE gifts (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    description VARCHAR NOT NULL,
    picture VARCHAR,
    price INT NOT NULL,
    stock INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    CONSTRAINT fk_gifts_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    guest_name VARCHAR NOT NULL,
    guest_email VARCHAR NOT NULL,
    amount INT NOT NULL,
    status order_status_enum NOT NULL DEFAULT 'PENDING',
    payment_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    wedding_id UUID NOT NULL,
    gift_id UUID NOT NULL,
    message_id UUID,
    CONSTRAINT fk_orders_gift FOREIGN KEY (gift_id) REFERENCES gifts(id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE RESTRICT,
    CONSTRAINT uq_orders_message UNIQUE (message_id),
    CONSTRAINT chk_order_status CHECK (status IN ('PENDING', 'PAID', 'FAILED', 'EXPIRED'))
);

CREATE TABLE wallets (
     id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
     pix_key VARCHAR(255),
     balance INTEGER NOT NULL DEFAULT 0,
     fee INTEGER NOT NULL DEFAULT 500,
     is_active BOOLEAN NOT NULL DEFAULT TRUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     wedding_id UUID NOT NULL UNIQUE,
     CONSTRAINT fk_wallet_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
      id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
      amount INTEGER NOT NULL,
      fee INTEGER NOT NULL,
      status transaction_status_enum NOT NULL DEFAULT 'PENDING',
      asaas_transfer_id VARCHAR(255),
      is_active BOOLEAN NOT NULL DEFAULT TRUE,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      wallet_id UUID NOT NULL,
      wedding_id UUID NOT NULL,
      CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE,
      CONSTRAINT fk_transaction_wedding FOREIGN KEY (wedding_id) REFERENCES weddings(id) ON DELETE CASCADE,
      CONSTRAINT chk_transaction_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))
);
