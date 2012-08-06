<?php

include_once "rnadb.php";
include_once "rna_services.php";

if (isset($_GET['populate'])) {
	addDirectory("../db/");
}
if (isset($_GET['search'])) {
	$arr = array();
	$arr["rows"] = getSequences($_POST);
	$arr["offset"] = $_POST["offset"];
	echo json_encode($arr);
}
if (isset($_GET['download'])) {
	$arr = explode(",", $_POST['selected']);
	if (count($arr) == 1 && $arr[0]=="") {
		echo json_encode(array("error"=>"No files are selected."));
	} else {
		$filenames = array();
		for($i=0;$i<count($arr);$i++) {
			$filenames[] = get_filename($arr[$i]);
		}
		echo json_encode(array("link"=>create_zip($filenames)));
	}
}
if (isset($_GET['downloadAll'])) {
	$arr = getSequences_db($_POST);
	if (count($arr) == 1 && $arr[0]=="") {
		echo json_encode(array("error"=>"No files to download."));
	} else {
		$filenames = array();
		for($i=0;$i<count($arr);$i++) {
			$row = $arr[$i];
			$filenames[] = $row["name"];
		}
		echo json_encode(array("link"=>create_zip($filenames)));
	}
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
		[natdenmin] => 0
		[natdenmax] => 1000
		[preddenmin] => 0
		[preddenmax] => 1000
		[stuffeddenmin] => 0
		[stuffeddenmax] => 1000
)
*/
if (isset($_GET['getSize'])) {
	$_POST['lenmin'] = $_POST['seqLength'][0];
	$_POST['lenmax'] = $_POST['seqLength'][1];
	$_POST['mfeaccmin'] = $_POST['mfeAccuracy'][0];
	$_POST['mfeaccmax'] = $_POST['mfeAccuracy'][1];
	$_POST['natdenmin'] = $_POST['natDensity'][0];
	$_POST['natdenmax'] = $_POST['natDensity'][1];
	$_POST['preddenmin'] = $_POST['predDensity'][0];
	$_POST['preddenmax'] = $_POST['predDensity'][1];
	$_POST['stuffeddenmin'] = $_POST['stuffedDensity'][0];
	$_POST['stuffeddenmax'] = $_POST['stuffedDensity'][1];
	$_POST['size'] = 100000;
	$size = getSize_db($_POST);
	echo json_encode(array("setId"=>$_POST['sizeId'], "setSize"=>$size));
}


// public
function getSequences($params) {
	$arr = getSequences_db($params);
	return $arr;
}

// private
function getSequence($id) {
	return array( "rid"=>$id ,"name" => "ecoli", 
			"family" => "5S", "seqLength" => "135",
			"mfeAcc" => "91%", "natDensity" => "73%",
			"predDensity" => "77%", "stuffedDensity" => "91%",
			"ambiguous" => 0, "alignment"=>"---acgu---a-c-g-u--" );
}

?>