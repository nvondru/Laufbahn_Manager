CREATE DATABASE `loginlaufbahn` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE loginlaufbahn;

CREATE TABLE `besprechung` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT 'Neue Besprechung',
  `datum` date NOT NULL,
  `kommentar` varchar(1000) DEFAULT NULL,
  `fk_id_person` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(500) NOT NULL,
  `fk_id_besprechung` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `vorname` varchar(45) NOT NULL,
  `beruf` varchar(45) NOT NULL,
  `abteilung` varchar(45) NOT NULL,
  `personalnummer` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
CREATE TABLE `laufbahn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schule_bez` varchar(100) DEFAULT 'Schule',
  `schule_comment` varchar(150) DEFAULT 'Kommentar',
  `lehre_bez` varchar(45) DEFAULT 'Ausbildungsstatus',
  `lehre_comment` varchar(150) DEFAULT 'Kommentar',
  `skills_bez` varchar(45) DEFAULT 'Skills',
  `skills_comment` varchar(150) DEFAULT 'Kommentar',
  `ausland_checked` tinyint(1) DEFAULT '0',
  `ausland_comment` varchar(150) DEFAULT 'Kommentar',
  `weiterbildung_checked` tinyint(4) DEFAULT '0',
  `weiterbildung_comment` varchar(150) DEFAULT 'Kommentar',
  `fk_id_person` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_id_person_idx` (`fk_id_person`),
  CONSTRAINT `fk_id_person` FOREIGN KEY (`fk_id_person`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;


INSERT INTO credentials(username, password) VALUES ("", "da39a3ee5e6b4b0d3255bfef95601890afd80709");

