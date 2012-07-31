<?php
// session stuff (application key?)

include_once 'main_db.php';

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

/*
	 Array
	(
			[family] => tRna,5S,16S,23S
			[ambiguous] => true
			[aligned] => true
			[lenmin] => 0
			[lenmax] => 3000
			[mfeaccmin] => 0
			[mfeaccmax] => 1000
			[name] =>
			[accession] =>
			[natdenmin] => 0
			[natdenmax] => 1000
			[preddenmin] => 0
			[preddenmax] => 1000
			[stuffeddenmin] => 0
			[stuffeddenmax] => 1000
	)
*/
function getSequences_db($params) 
{
	// TODO: validate input
	$con = connectToDB();
	$sql = 'SELECT * FROM `rna` JOIN `pred` ON `rna`.`rid`=`pred`.`rid` WHERE ';
	
	/*
	 * HANDLE FAMILY
	 */
	$params['family'] = strtolower($params['family']);
	$arr = explode(',', $params['family']);
	for($sum=0;$sum<count($arr);$sum++)
		$params[$arr[$sum]] = 1;
	$opened = $sum;
	$needOr = 0;
	$needAnd = $sum;
	if ($sum > 1)
		$sql .= '(';
	if (isset($params['5s']))
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
	if (isset($params['16s']))
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
	if (isset($params['23s']))
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
	if (isset($params['trna']))
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
	if (!$params['aligned'])
	{
		$sql .= '`rna`.`alignment`=1 AND ';
		$needAnd = 1;
	}
	
	/*
	 * HANDLE SEQUENCE LENGTH
	*/
	$sql .= '(`rna`.`seqlen` BETWEEN '.$params['lenmin'].' AND '.$params['lenmax'].') AND ';
	
	/*
	 * HANDLE FILENAME
	*/
	if ($params['name'] != '')
		$sql .= '`rna`.`filename` LIKE \'%'.$params['name'].'%\' AND ';
	
	/*
	 * HANDLE ACCESSION NUMBER
	*/
	if ($params['accession'] != '')
	$sql .= '`rna`.`accessionnum` LIKE \'%'.$params['accession'].'%\' AND';
	/*
	 * HANDLE RANGES
	 */
	$sql .= '(`rna`.`den` BETWEEN '.$params['natdenmin'].' AND '.$params['natdenmax'].') AND ';
	$sql .= '(`pred`.`pden` BETWEEN '.$params['preddenmin'].' AND '.$params['preddenmax'].') AND ';
	$sql .= '(`pred`.`sden` BETWEEN '.$params['stuffeddenmin'].' AND '.$params['stuffeddenmax'].');';
	
	$db_result = parseQueryResults(desql($sql));
	breakCon($con);
	return $db_result;
}

function parseQueryResults($db_result)
{
	$results = array();
	while($row = mysql_fetch_array($db_result))
		$results[] = parseJoinedRow($row);
	return $results;
}

/*
$rna['rid']
$rna['name']
$rna["accession"]
$rna["family"]
$rna["seqLength"]
$rna["mfeAcc"]
$rna["natDensity"]
$rna["predDensity"]
$rna["stuffedDensity"]
$rna["ambiguous"]
$rna["alignment"]
 */
function parseJoinedRow($row) {
	$rna = array();
	$rna['rid'] = $row['rid'];
	$rna["family"] = $row['family'];
	$rna["ambiguous"] = $row['ambiguous'];
	$rna["alignment"] = $row['alignment'];
	$rna["seqLength"] = $row['seqlen'];
	$rna['name'] = $row['filename'];
	$rna["accession"] = $row['accessionnum'];
	$rna["natDensity"] = $row['den'];
	$rna["mfeAcc"] = $row['acc'];
	$rna["predDensity"] = $row['pden'];
	$rna["stuffedDensity"] = $row['sden'];
	return $rna;
}
?>
