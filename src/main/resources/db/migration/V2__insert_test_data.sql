-- Insert engines
INSERT INTO engine (manufacturer, effect_output, model) VALUES
                                                            ('Radon-Ulzer', 620, '620C'),
                                                            ('Collor Pondrat', 600, 'Plug-F Mammoth Split-X'),
                                                            ('Irdani Performance Group', 580, 'IPG-X1131 LongTail'),
                                                            ('Balta-Trabaat', 700, 'T-990 Mk II'),
                                                            ('Keizar-Volvec', 550, 'KV9T9-B Wasp'),
                                                            ('Ord Pedrovia', 590, 'Ord Pedrovia Special');
-- Insert podracers
INSERT INTO podracer (engine_id, model, manufacturer, weight, max_speed) VALUES
                                                                             (1, 'Custom-built Podracer', 'Radon-Ulzer Repulsorlift', 1500, 947),
                                                                             (2, 'Collor Pondrat Plug-F Mammoth', 'Collor Pondrat', 1800, 829),
                                                                             (3, 'IPG-X1131 LongTail', 'Irdani Performance Group', 1600, 775),
                                                                             (4, 'BT310 Quadra', 'Balta-Trabaat', 2000, 940),
                                                                             (5, 'Voltec KT9 Wasp', 'Keizar-Volvec', 1700, 800),
                                                                             (6, 'Ord Pedrovia', 'Ord Pedrovia', 1650, 823);
-- Insert pit crews
INSERT INTO pit_crew (crew_name) VALUES
                                     ('Anakin''s Pit Crew'),
                                     ('Sebulba''s Pit Crew'),
                                     ('Teemto''s Pit Crew'),
                                     ('Ben Quadinaros'' Pit Crew'),
                                     ('Clegg''s Pit Crew'),
                                     ('Gasgano''s Pit Crew');

-- Insert droids
INSERT INTO droid (pit_crew_id, manufacturer, price) VALUES
                                                         -- Anakin Skywalker
                                                         (1, 'Industrial Automaton', 5000),
                                                         (1, 'Cybot Galactica', 4500),

                                                         -- Sebulba
                                                         (2, 'Serv-O-Droid', 6000),
                                                         (2, 'Serv-O-Droid', 6000),
                                                         (2, 'Serv-O-Droid', 6000),
                                                         (2, 'Serv-O-Droid', 6000),
                                                         (2, 'Serv-O-Droid', 6000),

                                                         -- Teemto Pagalies
                                                         (3, 'RoboTech', 4000),
                                                         (3, 'RoboTech', 4000),
                                                         (3, 'RoboTech', 4000),

                                                         -- Ben Quadinaros
                                                         (4, 'DroidWorks', 5500),
                                                         (4, 'DroidWorks', 5500),
                                                         (4, 'DroidWorks', 5500),
                                                         (4, 'DroidWorks', 5500),

                                                         -- Clegg Holdfast
                                                         (5, 'MechaCorp', 4200),
                                                         (5, 'MechaCorp', 4200),
                                                         (5, 'MechaCorp', 4200),

                                                         -- Gasgano
                                                         (6, 'AutoMech', 4800),
                                                         (6, 'AutoMech', 4800),
                                                         (6, 'AutoMech', 4800),
                                                         (6, 'AutoMech', 4800);

-- Insert racers
INSERT INTO pilot (podracer_id, pit_crew_id, name, species, home_planet) VALUES
                                                                             (1, 1, 'Anakin Skywalker', 'Human', 'Tatooine'),
                                                                             (2, 2, 'Sebulba', 'Dug', 'Malastare'),
                                                                             (3, 3, 'Teemto Pagalies', 'Veknoid', 'Moonus Mandel'),
                                                                             (4, 4, 'Ben Quadinaros', 'Toong', 'Tund'),
                                                                             (5, 5, 'Clegg Holdfast', 'Nosaurian', 'New Plympto'),
                                                                             (6, 6, 'Gasgano', 'Xexto', 'Troiken');