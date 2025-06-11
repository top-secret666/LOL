drop  database if exists university;
create database university DEFAULT CHARACTER SET utf8;

USE university;

DROP TABLE if exists students;
DROP TABLE IF EXISTS `groups`;
DROP TABLE IF EXISTS `leaders`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `university`.`leaders` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `name` VARCHAR(45) NULL,
                                        `surname` VARCHAR(45) NULL,
                                        `phone_number` VARCHAR(20) NULL,
                                        `position` VARCHAR(45) NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE INDEX `unique_leader` (`name`, `surname`,`phone_number`)
);

INSERT INTO `university`.`leaders` (`name`, `surname`, `phone_number`, `position`) VALUES
                                                                                       ('Alex', 'Johnson', '111222333', 'Senior Lecturer'),
                                                                                       ('Maria', 'Smith', '444555666', 'Professor'),
                                                                                       ('David', 'Brown', '777888999', 'Assistant Professor');

CREATE TABLE `university`.`groups` (
                                       `gr_id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `gr_name` VARCHAR(45) NULL,
                                       PRIMARY KEY (`gr_id`),
                                       UNIQUE INDEX `unique_group_name` (`gr_name`)
);

INSERT INTO `university`.`groups` (`gr_id`, `gr_name`) VALUES ('1', 'D');
INSERT INTO `university`.`groups` (`gr_id`, `gr_name`) VALUES ('2', 'S');

CREATE TABLE `university`.students (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NULL,
                                       `surname` VARCHAR(45) NULL,
                                       `phone_number` VARCHAR(20) NULL,
                                       `gr_id` BIGINT NULL,
                                       `leader_id` BIGINT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `unique_student` (`name`, `surname`,`phone_number`),
                                       CONSTRAINT `gk`
                                           FOREIGN KEY (`gr_id`)
                                               REFERENCES `university`.`groups` (`gr_id`)
                                               ON DELETE NO ACTION
                                               ON UPDATE NO ACTION,
                                       CONSTRAINT `leader_fk`
                                           FOREIGN KEY (`leader_id`)
                                               REFERENCES `university`.`leaders` (`id`)
                                               ON DELETE NO ACTION
                                               ON UPDATE NO ACTION
);

INSERT INTO `university`.students (`name`, `surname`, `phone_number`, `gr_id`, `leader_id`) VALUES
                                                                                                ('John', 'Smith', '123456789', 1, 1),
                                                                                                ('Emma', 'Johnson', '234567890', 1, 1),
                                                                                                ('Michael', 'Williams', '345678901', 2, 2),
                                                                                                ('Sarah', 'Brown', '456789012', 2, 2),
                                                                                                ('David', 'Jones', '567890123', 1, 3),
                                                                                                ('Lisa', 'Garcia', '678901234', 1, 3),
                                                                                                ('James', 'Miller', '789012345', 2, 1),
                                                                                                ('Jennifer', 'Davis', '890123456', 2, 1),
                                                                                                ('Robert', 'Rodriguez', '901234567', 1, 2),
                                                                                                ('Maria', 'Martinez', '012345678', 1, 2),
                                                                                                ('William', 'Anderson', '123123123', 2, 3),
                                                                                                ('Elizabeth', 'Taylor', '234234234', 2, 3),
                                                                                                ('Richard', 'Thomas', '345345345', 1, 1),
                                                                                                ('Patricia', 'Moore', '456456456', 1, 1),
                                                                                                ('Joseph', 'Jackson', '567567567', 2, 2),
                                                                                                ('Margaret', 'White', '678678678', 2, 2),
                                                                                                ('Charles', 'Harris', '789789789', 1, 3),
                                                                                                ('Susan', 'Clark', '890890890', 1, 3),
                                                                                                ('Thomas', 'Lewis', '901901901', 2, 1),
                                                                                                ('Jessica', 'Lee', '012012012', 2, 1);
CREATE TABLE users (
                       id INT NOT NULL AUTO_INCREMENT,
                       username varchar(64) NOT NULL,
                       password varchar(64) NOT NULL,
                       authority varchar(64) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE INDEX `unique_user` (`username`, `password`)
);

INSERT into `users` (id, username, password, authority) values
                                                            (1, 'admin', '$2a$10$jrryFNptnoGWwyWhxc47eeeHpin/LPOut7J221Xv4DB3qTswVcvJS',
                                                             'ROLE_ADMIN'),
                                                            (2, 'user', '$2a$10$I0BOCCDqRH6905RIlUmgd.2L008fmT3QvFtjEynyJQ2WoKDFRNGo6',
                                                             'ROLE_USER');