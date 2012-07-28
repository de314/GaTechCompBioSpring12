<?php 

if (isset($_GET['add_nat'])) {
	
}
if (isset($_GET['add_pred'])) {

}
if (isset($_GET['add_pair'])) {

}
if (isset($_GET['search'])) {

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
if (isset($_GET['getSize'])) {
	// TODO: parse the following
// 	family
// 	ambiguous
// 	aligned
// 	seqLength
// 	mfeAccuracy
// 	name
// 	accession
// 	natDensity
// 	predDensity
// 	stuffedDensity
	echo json_encode(array("setId"=>$_POST['sizeId'], "setSize"=>rand(0, 30000)));
}


// public
function getSequences($params) {
	$arr = array();
	$arr[] = getSequence(1);
	$arr[] = getSequence(2);
	$arr[] = getSequence(3);
	$arr[] = getSequence(4);
	$arr[] = getSequence(5);
	$arr[] = getSequence(6);
	$arr[] = getSequence(7);
	$arr[] = getSequence(8);
	$arr[] = getSequence(9);
	$arr[] = getSequence(10);
	$arr[] = getSequence(11);
	$arr[] = getSequence(12);
	$arr[] = getSequence(13);
	$arr[] = getSequence(14);
	$arr[] = getSequence(15);
	$arr[] = getSequence(16);
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