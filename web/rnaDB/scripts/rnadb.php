<?php
// session stuff (application key?)

// expecting: { rid, filename, accessionnum, seqlen, ambiguous, alignment, family, den }
function insertStructure($rna) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'INSERT INTO `rna` (`rid`, `filename`, `accessionnum`, `seqlen`, `ambiguous`, `alignment`, `family`, `den`) VALUES 
	(NULL ,  \''.$rna['filename'].'\',  \''.$rna['accessionnum'].'\', \''.$rna['seqlen'].'\', \''.$rna['ambiguous'].'\', \''.$rna['alignment'].'\', \''.$rna['family'].'\', \''.$rna['den'].'\');';
	$result = desql($sql);
	breakCon($con);
	return $result;
}

// expecting: { rid, accuracy, den, stuf }
function insertPrediction($pred) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'INSERT INTO `rna` (`predid`, `rid`, `accuracy`, `den`, `stuf`) VALUES 
	(NULL, \''.$pred['rid'].'\', \''.$pred['accuracy'].'\', \''.$pred['den'].'\', \''.$pred['stuf'].'\');';
	$result = desql($sql);
	breakCon($con);
	return $result;
}

function getStructureById($id) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'SELECT * FROM  `rna` WHERE  `rid`=$id';
	$result = desql($sql);
	breakCon($con);
	// TODO: return correct results
	return $result;
}

function getPredById($id) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'SELECT * FROM  `pred` WHERE  `predid`=$id';
	$result = desql($sql);
	breakCon($con);
	// TODO: return correct results
	return $result;
}

// expecting: { bool:5S, bool:16S, bool:23S, bool:trna, ambiguous, alignment, minSeqLen, maxSeqLen, minAcc, maxAcc,
// seqName, accessionNum, minNatDen, maxNatDen, minPredDen maxPredDen, minStufDen, maxStufDen }
function getSearchResults($params) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'SELECT * FROM `rna` LEFT JOIN `pred` ON `rna`.`rid`=`pred`.`rid` WHERE ';
	/*
	 * HANDLE ACCESSION NUMBER
	 */
	if ($params['accessionnum'] == '') 
	{
		$sql .= '`rna`.`accessionnum` LIKE \'%'.$params['accessionnum'].'%\';';
	} 
	else 
	{
		if ($params['filename'] != '')
			$sql .= '`rna`.`filename` LIKE \'%'.$params['filename'].'%\' AND ';
		/*
		 * HANDLE FAMILY
		 */
		$sum = $params['5S'] + $params['16S'] + $params['23S'] + $params['trna'];
		$opened = $sum;
		$needOr = 0;
		$needAnd = $sum;
		if ($sum > 1)
			$sql .= '(';
		if ($params['5S'])
		{
			$sql .= '`rna`.`family`=\'5S\' ';
			$sum--;
			$needOr = 1;
		}
		if ($needOr && $sum)
		{
			$sql .= 'OR ';
			$needOr = 0;
		}
		if ($params['16S'])
		{
			$sql .= '`rna`.`family`=\'16S\' ';
			$sum--;
			$needOr = 1;
		}
		if ($needOr && $sum)
		{
			$sql .= 'OR ';
			$needOr = 0;
		}
		if (!$sum && $opened)
		{
			$sql .= ') ';
			$opened = 0;
		}
		if ($params['23S'])
		{
			$sql .= '`rna`.`family`=\'23S\' ';
			$sum--;
			$needOr = 1;
		}
		if ($needOr && $sum)
		{
			$sql .= 'OR ';
			$needOr = 0;
		}
		if (!$sum && $opened)
		{
			$sql .= ') ';
			$opened = 0;
		}
		if ($params['trna'])
		{
			$sql .= '`rna`.`family`=\'trna\' ';
			$sum--;
			$needOr = 1;
		}
		if (!$sum && $opened)
		{
			$sql .= ') ';
			$opened = 0;
		}
		if ($needAnd) 
		{
			$sql .= 'AND ';
			$needAnd = 0;
		}
		/*
		 * HANDLE AMBIGUOUS and ALIGNMENT
		 */
		if (!$params['ambiguous'])
		{
			$sql .= '`rna`.`ambiguous`=0 AND ';
		}
		if (!$params['alignment'])
		{
			$sql .= '`rna`.`alignment`=1 AND ';
			$needAnd = 1;
		}
		/*
		 * HANDLE RANGES
		 */
		$sql .= '(`rna`.`seqlen` BETWEEN '.$params['minSeqLen'].' AND '.$params['maxSeqLen'].') AND ';
		$sql .= '(`rna`.`den` BETWEEN '.$params['minNatDen'].' AND '.$params['maxNatDen'].') AND ';
		$sql .= '(`pred`.`den` BETWEEN '.$params['minPredDen'].' AND '.$params['maxPredDen'].') AND ';
		$sql .= '(`pred`.`stuf` BETWEEN '.$params['minStufDen'].' AND '.$params['maxStufDen'].');';
	}
	$result = parseQueryResults(desql($sql));
	breakCon($con);
	// TODO: return correct results
	return $result;
}

function connectToDB()
{
	$host = "desposi1.fatcowmysql.com"; // Host name
	$username = "compbio"; // Mysql username
	$password = "compbio10"; // Mysql password
	$db = "compbio"; // Database name

	// Connect to server and select databse.
	$con = mysql_connect($host, $username, $password) or die('Could not connect. Go to <a href="adminLogin.php" class="links">Login Page</a>.');
	mysql_select_db("$db")or die('Cannot select DB. Go to <a href="adminLogin.php" class="links">Login Page</a>.');
	return $con;
}

function breakCon($con)
{
	return mysql_close($con);
}

function desql($sql)
{
	$sql = stripslashes($sql);
	//	$sql = mysql_real_escape_string($sql);
	return mysql_query($sql);
}

function desql_print($sql)
{
	$result = desql($sql);
	if(mysql_num_rows($result) > 0)
	{
		echo "<pre>";
		while($row = mysql_fetch_array($result))
		{
			print_r($row);
			echo "<br />";
		}
		echo "</pre>";
	}
}
?>
