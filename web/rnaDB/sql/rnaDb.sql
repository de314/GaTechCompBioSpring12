DROP DATABASE rDB;

CREATE DATABASE rDB;

USE rDB;

CREATE TABLE IF NOT EXISTS `rDB`.`rna` (
	`rid`       int(10) unsigned NOT NULL AUTO_INCREMENT,
	`filename`  varchar(100) NOT NULL,
	`type`      varchar(5) NOT NULL,
	`seqlen`    int(5),
	`ambiguous` int(1) NOT NULL,
	`family`    varchar(5) NOT NULL,
	`den`       double precision NOT NULL,
	PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `rDB`.`pred` (
        `predid`    INT UNSIGNED NOT NULL AUTO_INCREMENT,
        `rid`       INT UNSIGNED NOT NULL,
        `technique` VARCHAR(10) NOT NULL,
        `filename`  VARCHAR(100) NOT NULL,
        `acc`       double precision NOT NULL,
	`den`       double precision NOT NULL,
	`stuf`      double precision NOT NULL,
        PRIMARY KEY (`taid`),
        INDEX ( `rid` )
);

INSERT INTO `rDB`.`rna` (`filename`, `type`, `seqlen`, `seq`, `str`, `family`, `den`)
        VALUES ('test.ct', 'ct', 135, 0, '5S', 0.85);

INSERT INTO `rDB`.`pred` (`rid`, `technique`, `filename`, `acc`, `den`, `stuff`)
        VALUES (1, 'gtfold', 'test.ct', 0.90, 0.85, 0.96);

