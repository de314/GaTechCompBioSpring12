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

/*
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
/**/


if (isset($_GET["zip"])) {
	$stamp = time();
	$dir = "../uploads/".$stamp."/";
	mkdir($dir);
	$filename = basename( $_FILES[$field_name]['name']);
	$target_path = $dir . $filename;
	if(move_uploaded_file($_FILES[$field_name]['tmp_name'], $target_path)) {
		if (extract_zip($target_path, $dir)) {
			unlink($target_path);
			// first level of zip
			$ts_collection = array();
			$base = parse_training_set($dir, $filename);
			if ($base)
				$ts_collection[] = $base;
			
			// second level of zip
			$root = scandir($dir);
			foreach($root as $value)
			{
				if($value === '.' || $value === '..')
					continue;
				if(is_dir("$dir/$value")) {
					$ts = parse_training_set("$dir/$value", $value);
					if ($ts) {
						$ts_collection[] = $ts;
					}
					rmdir("$dir/$value");
				} else 
					unlink("$dir/$value");
			}
			/**/
			rmdir($dir);
			echo json_encode($ts_collection);
		}
	} else{
		echo "There was an error uploading the file, please try again!";
	}
}
/**/

function parse_training_set($dir, $name) {
	$root = scandir($dir);
	$ts = new TrainingSet($name, 0, 0);
	foreach($root as $value)
	{
		if($value === '.' || $value === '..')
			continue;
		if(is_file("$dir/$value")) {
			$ts->add_seq(parse_ct_file("$dir/$value", $value));
			unlink("$dir/$value");
		}
		// TODO: validate input!!
		if($value == 'info.txt') {
			$file_handle = fopen($value, "r");
			if (!feof($file_handle)) {
				$ts->name = fgets($file_handle);
				if (!feof($file_handle))
					$ts->color = fgets($file_handle);
			}
			fclose($file_handle);
		}
		/**/
	}
	//rmdir($dir);
	return $ts;
}

?>