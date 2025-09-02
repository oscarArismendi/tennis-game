-- Insert Players
INSERT INTO Player (firstname, lastname, email)
VALUES ('Rafael', 'Nadal', 'rafa.nadal@example.com');

INSERT INTO Player (firstname, lastname, email)
VALUES ('Novak', 'Djokovic', 'novak.djokovic@example.com');

-- Create a game where Nadal is server and Djokovic is receiver
INSERT INTO Game (server_id, receiver_id, server_score, receiver_score, advantage, state) VALUES (1, 2,0,0,'NONE','NOT_STARTED');