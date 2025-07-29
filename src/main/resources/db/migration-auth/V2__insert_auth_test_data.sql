-- Insert roles
INSERT INTO role (name) VALUES
                            ('GANGSTER'),
                            ('PILOT'),
                            ('SPECTATOR');

-- Insert users
INSERT INTO users (username, password) VALUES
                            ('Jabba_The_Hut', '$2a$12$N4NjU8UhnZ7UZRPoCmGRquUvCofUfpUtIj.x7i8YXzEAZM.MvZJ7y'),
                            ('Anakin_Skywalker', '$2y$12$YTImWVVbEfxj0fPEB1wxbu5TMQD5UBCjlA3CDDm23ADFrGfkCYFsO'),
                            ('Watto_The_Longnose', '$2y$12$cL0NyMrAP7YvKL2usJdU2eNDeui.CtqPArOejB0Tw1dUR2tgkLqpC');

-- Assign roles JABBA
INSERT INTO user_role (user_id, role_id)
                            SELECT u.id, r.id
                            FROM users u, role r
                            WHERE u.username = 'Jabba_The_Hut' AND r.name = 'GANGSTER';

-- Assign roles ANAKIN
INSERT INTO user_role (user_id, role_id)
                            SELECT u.id, r.id
                            FROM users u, role r
                            WHERE u.username = 'Anakin_Skywalker' AND r.name = 'PILOT';

-- Assign roles WATTO
INSERT INTO user_role (user_id, role_id)
                            SELECT u.id, r.id
                            FROM users u, role r
                            WHERE u.username = 'Watto_The_Longnose' AND  r.name = 'SPECTATOR';
