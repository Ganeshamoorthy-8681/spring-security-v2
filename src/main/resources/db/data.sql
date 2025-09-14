INSERT INTO permissions (name, description, action) VALUES
    ('IAM:USER:CREATE',            'Can create users', 'CREATE'),
    ('IAM:USER:READ',              'Can read user details', 'READ'),
    ('IAM:USER:UPDATE',            'Can update user details', 'UPDATE'),
    ('IAM:USER:DELETE',            'Can delete users', 'DELETE'),
    ('IAM:USER:LIST',              'Can list users', 'READ'),

    ('IAM:ROLE:CREATE',            'Can create roles', 'CREATE'),
    ('IAM:ROLE:READ',              'Can read role details', 'READ'),
    ('IAM:ROLE:UPDATE',            'Can update role details', 'UPDATE'),
    ('IAM:ROLE:DELETE',            'Can delete roles', 'DELETE'),
    ('IAM:ROLE:LIST',              'Can list roles', 'READ'),

    ('IAM:PERMISSION:LIST',        'Can list permissions', 'READ'),

    ('IAM:GROUP:CREATE',           'Can create groups', 'CREATE'),
    ('IAM:GROUP:READ',             'Can read group details', 'READ'),
    ('IAM:GROUP:UPDATE',           'Can update group details', 'UPDATE'),
    ('IAM:GROUP:DELETE',           'Can delete groups', 'DELETE'),
    ('IAM:GROUP:LIST',             'Can list groups', 'READ'),

    ('IAM:ACCOUNT:CREATE',         'Can create accounts', 'CREATE'),
    ('IAM:ACCOUNT:READ',           'Can read account details', 'READ'),
    ('IAM:ACCOUNT:UPDATE',         'Can update account details', 'UPDATE'),
    ('IAM:ACCOUNT:DELETE',         'Can delete accounts', 'DELETE'),
    ('IAM:ACCOUNT:LIST',           'Can list accounts', 'READ')
ON CONFLICT (name) DO NOTHING;