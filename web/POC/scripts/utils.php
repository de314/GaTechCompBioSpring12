<?php
include "rna.php";

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
	$handle = fopen($path, "r");
	if ($handle) {
		$buffer = fgets($handle);
		$vals = preg_split("/\s+/", $buffer);
		if ($vals[0] == "Filename:") {
			fgets($handle);
			fgets($handle);
			fgets($handle);
			$buffer = fgets($handle);
			$vals = preg_split("/\s+/", $buffer);
		}
		$total = intval($vals[0]);
		$seq = "";
		$str = "";
		while ($buffer = fgets($handle)) {
			$vals = preg_split("/\s+/", $buffer);
			if (count($vals) >= 5) {
				$seq .= $vals[1];
				$ind = intval($vals[4]);
				$str .= $ind ? ($ind > strlen($str) ? "(" : ")") : ".";
			}
		}
		if (!feof($handle)) {
			echo "Error: unexpected fgets() fail\n";
			return 0;
		}
		fclose($handle);
		if ($total != strlen($str)) {
			echo "Error: Length mismatch\n" . $seq . "\n" . $str;
			return 0;
		}
		return new RNAobj($filename, $seq, $str);
	}
}

function array_to_db($array) {
	
}
?>
