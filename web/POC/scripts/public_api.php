<?php
include "utils.php";

$field_name = "uploaded_file";

if (isset($_GET["upload"])) {
	$target_path = "../uploads/";
	$filename = basename( $_FILES[$field_name]['name']);
	$target_path = $target_path . $filename;
	if(move_uploaded_file($_FILES[$field_name]['tmp_name'], $target_path)) {
		$rna = parse_ct_file($target_path, $filename);
		if ($rna)
			$rna->json();
		else
			echo "Error: parsing file.";
	} else{
		echo "There was an error uploading the file, please try again!";
	}
}

if (isset($_GET["zip"])) {
	$stamp = time();
	$dir = "../uploads/".$stamp."/";
	mkdir($dir);
	$filename = basename( $_FILES[$field_name]['name']);
	$target_path = $dir . $filename;
	if(move_uploaded_file($_FILES[$field_name]['tmp_name'], $target_path)) {
		if (extract_zip($target_path, $dir)) {
			unlink($target_path);
			$root = scandir($dir);
			$rna_collection = array();
			foreach($root as $value)
			{
				if($value === '.' || $value === '..')
					continue;
				if(is_file("$dir/$value")) {
					$rna = parse_ct_file($dir . $value, $value);
					if ($rna)
						$rna_collection[] = $rna->as_array();
					else 
						echo "<br />Error: parsing file - '" . $dir . $value . "' - '" . $rna . "'";
				}
				unlink("$dir/$value");
			}
			rmdir($dir);
			echo json_encode($rna_collection);
		}
	} else{
		echo "There was an error uploading the file, please try again!";
	}
}

?>