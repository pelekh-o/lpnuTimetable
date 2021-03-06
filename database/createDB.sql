-- MySQL Script generated by MySQL Workbench
-- Mon Nov  6 15:44:16 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema timetable
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema timetable
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `timetable` DEFAULT CHARACTER SET utf8 ;
USE `timetable` ;

-- -----------------------------------------------------
-- Table `timetable`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timetable`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userID` INT(11) NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `username` VARCHAR(45) NULL DEFAULT NULL,
  `instituteTitle` VARCHAR(45) NULL DEFAULT NULL,
  `groupTitle` VARCHAR(45) NULL DEFAULT NULL,
  `isRemembered` TINYINT(1) NULL DEFAULT NULL,
  `regDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`, `userID`),
  UNIQUE INDEX `userID_UNIQUE` (`userID` ASC))
  ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `timetable`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timetable`.`chat` (
  `chatID` INT(11) NOT NULL,
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `isGroupChat` TINYINT(1) NOT NULL,
  `isUserChat` TINYINT(1) NOT NULL,
  `userID` INT(11) NOT NULL,
  PRIMARY KEY (`chatID`, `id`, `userID`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_chat_users1_idx` (`userID` ASC),
  CONSTRAINT `fk_chat_users1`
  FOREIGN KEY (`userID`)
  REFERENCES `timetable`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `timetable`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timetable`.`message` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `messageID` INT(11) NOT NULL,
  `messageText` MEDIUMTEXT NULL DEFAULT NULL,
  `chatID` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_message_chat1_idx` (`chatID` ASC),
  CONSTRAINT `fk_message_chat1`
  FOREIGN KEY (`chatID`)
  REFERENCES `timetable`.`chat` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB
  AUTO_INCREMENT = 36
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `timetable`.`answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timetable`.`answer` (
  `answerID` INT(11) NOT NULL AUTO_INCREMENT,
  `answerText` MEDIUMTEXT NULL DEFAULT NULL,
  `messageID` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`answerID`),
  INDEX `fk_answer_message_idx` (`messageID` ASC),
  CONSTRAINT `fk_answer_message`
  FOREIGN KEY (`messageID`)
  REFERENCES `timetable`.`message` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
  ENGINE = InnoDB
  AUTO_INCREMENT = 37
  DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
