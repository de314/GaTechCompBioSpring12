<?php
include "rna.php";

$error = '';

function extract_zip($filename, $extract_dir) {
	$zip = new ZipArchive;
	$res = $zip->open($filename);
	if ($res === TRUE) {
		$zip->extractTo($extract_dir);
		$zip->close();
		return 1;
	} else {
		return 0;
	}
}

function parse_ct_file($path, $filename) {
	$error = "";
	$handle = fopen($path, "r");
	if ($handle) {
		$buffer = fgets($handle);
		$vals = preg_split("/\s+/", $buffer);
		if ($vals[0] == "Filename:") {
			fgets($handle);
			fgets($handle);
			fgets($handle);
			$buffer = fgets($handle);
			$vals = preg_split("/\\s+/", $buffer);
		}
		$total = intval($vals[0] == '' ? $vals[1] : $vals[0]);
		$seq = "";
		$str = "";
		while ($buffer = fgets($handle)) {
			$vals = preg_split("/\s+/", $buffer);
			if (count($vals) >= 5) {
				$seq .= $vals[0] == '' ? $vals[2] : $vals[1];
				$ind = $vals[0] == '' ? $vals[5] : intval($vals[4]);
				$str .= $ind ? ($ind > strlen($str) ? "(" : ")") : ".";
			}
		}
		if (!feof($handle)) {
			// "Error: unexpected fgets() fail\n";
			$error = "Error: Could not open file - $handle";
			return 0;
		}
		fclose($handle);
		if ($total != strlen($str)) {
			// echo "Error: Length mismatch\n" . $seq . "<br />\n" . $str;
			// echo "<br />count: $total\n<br />Sequence Length" . strlen($str);
			$error = "Error: Length mismatch - count=$total  and Sequence Length=" . strlen($str);
			return 0;
		} 
		return new RNAobj($filename, $seq, $str);
	}
}

function array_to_db($array) {
	
}
?>
