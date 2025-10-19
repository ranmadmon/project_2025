/* ===== TaskFlow schema (Hibernate mappings) for primesec_database ===== */
CREATE DATABASE IF NOT EXISTS primesec_database CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE primesec_database;

SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET FOREIGN_KEY_CHECKS=0;

/* ---------- ROLES ---------- */
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- USERS ---------- */
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `username` VARCHAR(255) DEFAULT NULL,
                         `password` VARCHAR(255) DEFAULT NULL,
                         `first_name` VARCHAR(255) DEFAULT NULL,
                         `last_name` VARCHAR(255) DEFAULT NULL,
                         `email` VARCHAR(255) DEFAULT NULL,
                         `phone` VARCHAR(255) DEFAULT NULL,
                         `otp` VARCHAR(255) DEFAULT NULL,
                         `pass_recovery` VARCHAR(255) DEFAULT NULL,
                         `role_id` INT DEFAULT NULL,
                         `team_id` INT DEFAULT NULL,
                         `status` VARCHAR(20) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK_users_role` (`role_id`),
                         KEY `FK_users_team` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- ABILITIES ---------- */
DROP TABLE IF EXISTS `abilities`;
CREATE TABLE `abilities` (
                             `id` INT NOT NULL AUTO_INCREMENT,
                             `name` VARCHAR(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- TASKS ---------- */
DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(255) DEFAULT NULL,
                         `average_time` INT DEFAULT NULL,
                         `status` VARCHAR(20) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- CLIENTS ---------- */
DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
                           `id` INT NOT NULL AUTO_INCREMENT,
                           `name` VARCHAR(255) DEFAULT NULL,
                           `manager_id` INT DEFAULT NULL,
                           `status` VARCHAR(20) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK_clients_manager` (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- TASK REQUIREMENTS ---------- */
DROP TABLE IF EXISTS `task_requirements`;
CREATE TABLE `task_requirements` (
                                     `task_id` INT NOT NULL,
                                     `abilities` VARCHAR(255) DEFAULT NULL,
                                     PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- USER ABILITIES ---------- */
DROP TABLE IF EXISTS `user_abilities`;
CREATE TABLE `user_abilities` (
                                  `user_id` INT NOT NULL,
                                  `abilities` VARCHAR(255) DEFAULT NULL,
                                  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- TEAMS ---------- */
DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(255) DEFAULT NULL,
                         `head_id` INT DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK_teams_head` (`head_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- TEAM MEMBERS ---------- */
DROP TABLE IF EXISTS `team_members`;
CREATE TABLE `team_members` (
                                `team_id` INT NOT NULL,
                                `user_id` INT NOT NULL,
                                PRIMARY KEY (`team_id`,`user_id`),
                                KEY `FK_tm_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- TASK ASSIGNMENTS ---------- */
DROP TABLE IF EXISTS `task_assignments`;
CREATE TABLE `task_assignments` (
                                    `id` INT NOT NULL AUTO_INCREMENT,
                                    `task_id` INT DEFAULT NULL,
                                    `client_id` INT DEFAULT NULL,
                                    `user_id` INT DEFAULT NULL,
                                    `hours_required` INT DEFAULT NULL,
                                    `status` VARCHAR(255) DEFAULT NULL,
                                    `assigned_date` DATE DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `FK_ta_task` (`task_id`),
                                    KEY `FK_ta_client` (`client_id`),
                                    KEY `FK_ta_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* ---------- ASSIGNMENT PARTICIPANT ENTITY ---------- */
DROP TABLE IF EXISTS `assignmentParticipantEntity`;
CREATE TABLE `assignmentParticipantEntity` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `task_id` INT DEFAULT NULL,
                                               `user_id` INT DEFAULT NULL,
                                               `hours_assigned` INT DEFAULT NULL,
                                               PRIMARY KEY (`id`),
                                               KEY `FK_ap_taskassign` (`task_id`),
                                               KEY `FK_ap_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS=1;

/* ---------- ADD FKs ---------- */
ALTER TABLE `users`
    ADD CONSTRAINT `FK_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`),
    ADD CONSTRAINT `FK_users_team` FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`);

ALTER TABLE `clients`
    ADD CONSTRAINT `FK_clients_manager` FOREIGN KEY (`manager_id`) REFERENCES `users`(`id`);

ALTER TABLE `teams`
    ADD CONSTRAINT `FK_teams_head` FOREIGN KEY (`head_id`) REFERENCES `users`(`id`);

ALTER TABLE `team_members`
    ADD CONSTRAINT `FK_tm_team` FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`),
    ADD CONSTRAINT `FK_tm_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`);

ALTER TABLE `task_requirements`
    ADD CONSTRAINT `FK_tr_task` FOREIGN KEY (`task_id`) REFERENCES `tasks`(`id`);

ALTER TABLE `user_abilities`
    ADD CONSTRAINT `FK_ua_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`);

ALTER TABLE `task_assignments`
    ADD CONSTRAINT `FK_ta_task` FOREIGN KEY (`task_id`) REFERENCES `tasks`(`id`),
    ADD CONSTRAINT `FK_ta_client` FOREIGN KEY (`client_id`) REFERENCES `clients`(`id`),
    ADD CONSTRAINT `FK_ta_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`);

ALTER TABLE `assignmentParticipantEntity`
    ADD CONSTRAINT `FK_ap_taskassign` FOREIGN KEY (`task_id`) REFERENCES `task_assignments`(`id`),
    ADD CONSTRAINT `FK_ap_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`);

/* ===================== SEED DATA ===================== */
SET FOREIGN_KEY_CHECKS=0;

INSERT INTO `roles` (`id`,`name`) VALUES
                                      (1,'worker'),
                                      (2,'teamLeader'),
                                      (3,'admin'),
                                      (4,'admin teamLeader');

INSERT INTO `teams` (`id`,`name`,`head_id`) VALUES
    (1,'team1',NULL);

INSERT INTO `users`
(`id`,`username`,`password`,`first_name`,`last_name`,`email`,`phone`,`otp`,`pass_recovery`,`role_id`,`team_id`,`status`)
VALUES
    (5,'admin','98BB86E63389AA9EE3E38A21C24D2B84','admin','admin',NULL,'0527906905',NULL,NULL,4,1,NULL);

UPDATE `teams` SET `head_id` = 5 WHERE `id` = 1;

ALTER TABLE `roles` AUTO_INCREMENT = 5;
ALTER TABLE `users` AUTO_INCREMENT = 6;
ALTER TABLE `teams` AUTO_INCREMENT = 2;

SET FOREIGN_KEY_CHECKS=1;

/* ===== End of primesec_database schema ===== */
