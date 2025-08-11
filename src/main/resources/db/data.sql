INSERT INTO permissions (name, description) VALUES
    ('IAM:USER:CREATE',            'Can create users'),
    ('IAM:USER:READ',              'Can read user details'),
    ('IAM:USER:UPDATE',            'Can update user details'),
    ('IAM:USER:DELETE',            'Can delete users'),
    ('IAM:USER:LIST',              'Can list users'),

    ('IAM:ROLE:CREATE',            'Can create roles'),
    ('IAM:ROLE:READ',              'Can read role details'),
    ('IAM:ROLE:UPDATE',            'Can update role details'),
    ('IAM:ROLE:DELETE',            'Can delete roles'),
    ('IAM:ROLE:LIST',              'Can list roles'),

    ('IAM:PERMISSION:LIST',        'Can list permissions'),

    ('IAM:GROUP:CREATE',           'Can create groups'),
    ('IAM:GROUP:READ',             'Can read group details'),
    ('IAM:GROUP:UPDATE',           'Can update group details'),
    ('IAM:GROUP:DELETE',           'Can delete groups'),
    ('IAM:GROUP:LIST',             'Can list groups'),

    ('IAM:ACCOUNT:CREATE',         'Can create accounts'),
    ('IAM:ACCOUNT:READ',           'Can read account details'),
    ('IAM:ACCOUNT:UPDATE',         'Can update account details'),
    ('IAM:ACCOUNT:DELETE',         'Can delete accounts'),
    ('IAM:ACCOUNT:LIST',           'Can list accounts')
ON CONFLICT (name) DO NOTHING;
