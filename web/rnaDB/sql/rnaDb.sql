DROP DATABASE rnadb;

CREATE DATABASE rnadb;

USE rnadb;

CREATE TABLE IF NOT EXISTS `rnadb`.`rna` (
	`rid`       int(10) unsigned NOT NULL AUTO_INCREMENT,
	`family`    varchar(5) NOT NULL,
	`ambiguous` int(1) NOT NULL,
	`alignment` int(1) NOT NULL,
	`seqlen`    int(5),
	`filename`  varchar(100) NOT NULL,
	`accessionnum`  varchar(40) NOT NULL,
	`den`       double precision NOT NULL,
	PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `rnadb`.`pred` (
        `predid`    INT UNSIGNED NOT NULL AUTO_INCREMENT,
        `rid`       INT UNSIGNED NOT NULL,
        `technique` VARCHAR(10) NOT NULL,
        `filename`  VARCHAR(100) NOT NULL,
        `acc`       double precision NOT NULL,
	`pden`       double precision NOT NULL,
	`sden`      double precision NOT NULL,
        PRIMARY KEY (`predid`),
        INDEX ( `rid` )
);

INSERT INTO  `rnadb`.`rna` (`rid` ,`family` ,`ambiguous` ,`alignment` ,
			`seqlen` ,`filename` ,`accessionnum` ,`den`
		) VALUES (
			NULL ,  'tRNA',  '0',  '0',  '150',  'test.ct',  
			'ABC-1234',  '0.85' 
		);

INSERT INTO  `rnadb`.`pred` (`predid` ,`rid` ,`technique` ,`filename` ,
			`acc` ,`pden` ,`sden`
		) VALUES (
			NULL ,  '1',  'gtfold',  'test_pred.ct',  '0.95',  
			'0.90',  '0.944'
		);
