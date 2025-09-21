-- ========================
-- Accounts
-- ========================
CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    type VARCHAR(50),
    status VARCHAR(50),
    additional_attributes JSONB,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by TEXT,
    updated_by TEXT
);

-- ========================
-- Users
-- ========================
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    middle_name VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    type VARCHAR(50),
    status VARCHAR(50),
    account_id INT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    additional_attributes JSONB,
    last_login TIMESTAMPTZ,
    current_login TIMESTAMPTZ,
    failed_login_attempts INT DEFAULT 0,
    last_failed_login TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by TEXT,
    updated_by TEXT,
    deleted_by TEXT,
    is_root boolean,
    UNIQUE(email, account_id)
);

-- Index for fast login
CREATE INDEX IF NOT EXISTS idx_users_account_email ON users(account_id, email);

-- ========================
-- Roles
-- ========================
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    account_id INT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by TEXT,
    updated_by TEXT,
    UNIQUE(name, account_id)
);

CREATE INDEX IF NOT EXISTS idx_roles_account_name ON roles(account_id, name);

-- ========================
-- Permissions
-- ========================
CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    action VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- User-Roles (Many-to-Many)
-- ========================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX IF NOT EXISTS idx_user_roles_user ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON user_roles(role_id);

-- ========================
-- Role-Permissions (Many-to-Many)
-- ========================
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE INDEX IF NOT EXISTS idx_role_permissions_role ON role_permissions(role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_perm ON role_permissions(permission_id);

-- ========================
-- OTP Codes
-- ========================
CREATE TABLE IF NOT EXISTS otp_codes (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE, -- nullable for signup OTP
    email VARCHAR(255),
    otp VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    CHECK (user_id IS NOT NULL OR email IS NOT NULL) -- must have one
);

CREATE INDEX IF NOT EXISTS idx_otp_user_expires ON otp_codes(user_id, expires_at, used);

-- ========================
-- Activity Logs
-- ========================
CREATE TABLE IF NOT EXISTS activity_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    user_email TEXT, -- optional redundant copy
    account_id INT REFERENCES accounts(id) ON DELETE CASCADE,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id INT,
    description TEXT,
    ip_address TEXT,
    user_agent TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- Updated_at Trigger Function
-- ========================
-- Function to auto-update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS '
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
' LANGUAGE plpgsql;


-- Triggers
DROP TRIGGER IF EXISTS trigger_update_users ON users;
CREATE TRIGGER trigger_update_users
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS trigger_update_accounts ON accounts;
CREATE TRIGGER trigger_update_accounts
BEFORE UPDATE ON accounts
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS trigger_update_roles ON roles;
CREATE TRIGGER trigger_update_roles
BEFORE UPDATE ON roles
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

