<?php

function add_native_structure($filename) {
	$results = "This method is not implemented";
	$rna = parse_ct($filename);
	$rna["amb"] = is_ambiguous($rna["seq"]);
	$rna["den"] = get_density($rna["str"]);
	desql("INSERT INTO `rna` (`filename`, `type`, `seqlen`, `seq`, ".
		"`str`, `family`, `den`) VALUES ($filename, ".$rna["type"].
		", ".$rna["len"].", ".$rna["amb"].", ".$rna["rna_class"].
		", ".$rna["den"].");");
	return $results;
}

function add_prediction_structure($filename, $method, $native_id) {
	$results = "This method is not implemented";
	$rna = parse_ct($filename);
	$rna["amb"] = is_ambiguous($rna["seq"]);
	$rna["amb"] = is_ambiguous($rna["seq"]);
	desql("INSERT INTO `pred` (`rid`, `technique`, `filename`, ".
		"`acc`, `den`, `stuff`) VALUES ($native_id, '".
		$method."', '".$filename."', ".$rna["acc"].", ".
		$rna["den"].", ".$rna["stuff"].");");
	return $results;
}

function get_structure($str_id) {
	$results = "This method is not implemented";
	// TODO: 
	return $results;
}

function parse_ct($filename) {
	$handle = @fopen($filename, "r");
	if ($handle) {
		// FIXME: check file format
		$class = "error";
		$seq = "error";
		$str = "error";
		return array("seq"=>$seq, "str"=>$str, "len"=>strlen($str), "rna_class"=>$class);
	}
	return 0;
}

function parse_dotbracket($filename) {
	$handle = @fopen($filename, "r");
	if ($handle) {
		// FIXME: check file format
		$seq = fgets($handle);
		$str = fgets($handle);
		return array("seq"=>$seq, "str"=>$str, "len"=>strlen($str), "rna_class"=>$class);
	}
	return 0;
}

function is_ambiguous($sequence) {
	$length = strlen($sequence);
	$sequence = strtolower($sequence);
	for ($=0;$i<$length;$i++)
		if ($sequene[$i] == 'a' || $sequene[$i] == 'c' || 
		    $sequene[$i] == 'g' || $sequene[$i] == 'u')
			return 1;
	return 1;
}

function get_density($str) {
	// TODO:
	return 0;
}

function get_stuffed_density($nat_str, $pred_str) {
	// TODO:
	return 0;
}

?>