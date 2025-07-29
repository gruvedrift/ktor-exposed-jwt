CREATE TABLE IF NOT EXISTS engine (
                                      id SERIAL PRIMARY KEY,
                                      manufacturer TEXT,
                                      effect_output INTEGER, -- megawatts
                                      model TEXT
);

CREATE TABLE IF NOT EXISTS podracer (
                                      id SERIAL PRIMARY KEY,
                                      engine_id INTEGER REFERENCES engine(id) ON DELETE SET NULL,
                                      model TEXT NOT NULL,
                                      manufacturer TEXT NOT NULL,
                                      weight INTEGER,
                                      max_speed INTEGER
);

CREATE TABLE IF NOT EXISTS pit_crew (
                                      id SERIAL PRIMARY KEY,
                                      crew_name TEXT
);

CREATE TABLE IF NOT EXISTS droid (
                                      id SERIAL PRIMARY KEY,
                                      pit_crew_id INTEGER REFERENCES pit_crew(id) ON DELETE SET NULL,
                                      manufacturer TEXT,
                                      price INTEGER
);

CREATE TABLE IF NOT EXISTS pilot (
                                      id SERIAL PRIMARY KEY,
                                      podracer_id INTEGER REFERENCES podracer(id) ON DELETE SET NULL,
                                      pit_crew_id INTEGER REFERENCES pit_crew(id) ON DELETE SET NULL,
                                      name TEXT NOT NULL,
                                      species TEXT NOT NULL,
                                      home_planet TEXT
);