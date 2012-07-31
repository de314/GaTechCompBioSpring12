<?php 

include_once "rnadb.php";

if (isset($_GET['add_nat'])) {
	
}
if (isset($_GET['add_pred'])) {

}
if (isset($_GET['add_pair'])) {

}
if (isset($_GET['search'])) {
	// TODO
}
if (isset($_GET['downloadAll'])) {
	// TODO
	echo json_encode(array("link"=>"http://rnadb.math.gatech.edu/downloads/072720121847_all_0.zip"));
}
if (isset($_GET['downloadSelected'])) {
	// TODO
	$arr = explode(",", $_GET['selected']);
	for($i=0;$i<count($arr);$i++)
		$arr[$i] = $arr[$i];
	echo json_encode(array("link"=>"http://rnadb.math.gatech.edu/downloads/072720121847_selected_0.zip"));
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
	$arr = getSequences_db($_POST);
	echo json_encode(array("setId"=>$_POST['sizeId'], "setSize"=>count($arr)));
}


// public
function getSequences($params) {
	$arr = getSequences_db($params);
	return $arr;
}

// private
function getSequence($id) {
	return array( "rid"=>$id ,"name" => "ecoli", "accession" => "ABC-1234",
			"family" => "5S", "seqLength" => "135",
			"mfeAcc" => "91%", "natDensity" => "73%",
			"predDensity" => "77%", "stuffedDensity" => "91%",
			"ambiguous" => 0, "alignment"=>"---acgu---a-c-g-u--" );
}

?>