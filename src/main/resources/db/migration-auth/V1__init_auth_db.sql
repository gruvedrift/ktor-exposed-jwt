CREATE TABLE IF NOT EXISTS users (
                                    id SERIAL PRIMARY KEY,
                                    username TEXT NOT NULL,
                                    password TEXT NOT NULL -- encrypted with bcrypt
);

CREATE TABLE IF NOT EXISTS role (
                                    id SERIAL PRIMARY KEY,
                                    name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role (
                                    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                    role_id INT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
                                    PRIMARY KEY (user_id, role_id)
);
