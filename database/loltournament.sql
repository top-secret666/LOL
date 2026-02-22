drop  database if exists loltournament;
create database loltournament DEFAULT CHARACTER SET utf8;

USE loltournament;

DROP TABLE if exists tournaments;
DROP TABLE if exists tournament_teams;
DROP TABLE IF EXISTS `players`;
DROP TABLE IF EXISTS `teams`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS tournaments (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    creator_id BIGINT,
    FOREIGN KEY (creator_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS teams (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL UNIQUE,
    tag VARCHAR(5) NOT NULL UNIQUE,
    logo VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS players (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nickname VARCHAR(50) NOT NULL UNIQUE,
                                       real_name VARCHAR(100),
                                       role VARCHAR(20) NOT NULL,
                                       player_rank VARCHAR(50),
                                       team_id BIGINT,
                                       FOREIGN KEY (team_id) REFERENCES teams(id)
);


CREATE TABLE IF NOT EXISTS tournament_teams (
                                                tournament_id BIGINT NOT NULL,
                                                team_id BIGINT NOT NULL,
                                                PRIMARY KEY (tournament_id, team_id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
    );

INSERT INTO users (username, email, password, role)
VALUES ('admin', 'admin@example.com', '$2a$10$jrryFNptnoGWwyWhxc47eeeHpin/LPOut7J221Xv4DB3qTswVcvJS', 'ROLE_ADMIN');

INSERT INTO users (username, email, password, role)
VALUES
    ('manager', 'manager@example.com', '$2a$10$jrryFNptnoGWwyWhxc47eeeHpin/LPOut7J221Xv4DB3qTswVcvJS', 'ROLE_MANAGER'),
    ('user', 'user@example.com', '$2a$10$jrryFNptnoGWwyWhxc47eeeHpin/LPOut7J221Xv4DB3qTswVcvJS', 'ROLE_USER');

INSERT INTO teams (name, tag, logo)
VALUES
    ('Team Solo Mid', 'TSM', 'tsm_logo.png'),
    ('Cloud9', 'C9', 'c9_logo.png'),
    ('Team Liquid', 'TL', 'tl_logo.png'),
    ('Fnatic', 'FNC', 'fnc_logo.png');

INSERT INTO players (nickname, real_name, role, player_rank, team_id)
VALUES
    ('Bjergsen', 'Søren Bjerg', 'MID', 'BRONZE', 1),
    ('Doublelift', 'Yiliang Peng', 'ADC', 'BRONZE', 1),
    ('Blaber', 'Robert Huang', 'JUNGLE', 'BRONZE', 2),
    ('Jensen', 'Nicolaj Jensen', 'MID', 'BRONZE', 3),
    ('Rekkles', 'Martin Larsson', 'ADC', 'BRONZE', 4);


INSERT INTO tournaments (name, start_date, end_date, description, status, creator_id)
VALUES
    ('Summer Split 2023', '2023-06-01', '2023-08-30', 'Summer tournament for League of Legends', 'COMPLETED', 1),
    ('Winter Championship 2023', '2023-12-01', '2024-02-28', 'Winter championship for League of Legends', 'REGISTRATION', 2);

INSERT INTO tournament_teams (tournament_id, team_id)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 1), (2, 2), (2, 3);

UPDATE players SET role = 'Top' WHERE role = 'TOP';
UPDATE players SET role = 'Jungle' WHERE role = 'JUNGLE';
UPDATE players SET role = 'Mid' WHERE role = 'MID';
UPDATE players SET role = 'Adc' WHERE role = 'ADC';
UPDATE players SET role = 'Support' WHERE role = 'SUPPORT';

UPDATE players SET player_rank = 'Iron' WHERE player_rank = 'IRON';
UPDATE players SET player_rank = 'Bronze' WHERE player_rank = 'BRONZE';
UPDATE players SET player_rank = 'Silver' WHERE player_rank = 'SILVER';
UPDATE players SET player_rank = 'Gold' WHERE player_rank = 'GOLD';
UPDATE players SET player_rank = 'Platinum' WHERE player_rank = 'PLATINUM';
UPDATE players SET player_rank = 'Diamond' WHERE player_rank = 'DIAMOND';
UPDATE players SET player_rank = 'Master' WHERE player_rank = 'MASTER';
UPDATE players SET player_rank = 'Grandmaster' WHERE player_rank = 'GRANDMASTER';
UPDATE players SET player_rank = 'Challenger' WHERE player_rank = 'CHALLENGER';

SELECT DISTINCT role FROM players;
SELECT DISTINCT player_rank FROM players;
