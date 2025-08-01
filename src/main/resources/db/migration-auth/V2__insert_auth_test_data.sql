-- Insert roles
INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('PILOT'),
                            ('SPECTATOR');

-- Insert users
INSERT INTO users (username, password) VALUES
                            ('Jabba_The_Hut', '$2y$12$Ody6RpWvPTLr0d00oZnjxuf6oFht0wLLf4/2qMHcViVsIm00JRSue'), -- leia-is-my-slave
                            ('Anakin_Skywalker', '$2y$12$YTImWVVbEfxj0fPEB1wxbu5TMQD5UBCjlA3CDDm23ADFrGfkCYFsO'), -- raceKing666
                            ('Watto_The_Longnose', '$2y$12$5NQm/HV/HFwfq7uTu.hih.cbsdZbjjCCFmNEelkrYyvQkZLJqE8qK'); -- gummitryne-fina-abuser

-- Assign roles JABBA
INSERT INTO user_role (user_id, role_id)
                            SELECT u.id, r.id
                            FROM users u, role r
                            WHERE u.username = 'Jabba_The_Hut' AND r.name = 'ADMIN';

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
