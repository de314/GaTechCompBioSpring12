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
			echo json_encode(array("errors"=>"Error: parsing file: ".$_FILES[$field_name]['name'].", please try again!"));
	} else 
		echo json_encode(array("errors"=>"Server error uploading file ".$_FILES[$field_name]['name'].", please try again!"));
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
			// first level of zip
			$ts_collection = array();
			$base = parse_training_set($dir, $filename, 1);
			if ($base)
				$ts_collection[] = $base;
			
			// second level of zip
			$root = scandir($dir);
			foreach($root as $value)
			{
				if($value === '.' || $value === '..')
					continue;
				if(is_dir("$dir/$value")) {
					$ts = parse_training_set("$dir/$value", $value, 0);
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
		echo json_encode(array("errors"=>"Server error uploading file ".$_FILES[$field_name]['name'].", please try again!"));
	}
	
}
/**/

function parse_training_set($dir, $name, $is_base) {
	$root = scandir($dir);
	$ts = new TrainingSet($name, 0, 0);
	foreach($root as $value)
	{
		if($value === '.' || $value === '..')
			continue;
		if (is_dir("$dir/$value") && !$is_base) {
			// Invalid directory structure
			// TODO: Return error message
			break;
		}
		if(is_file("$dir/$value") && strpos($value, "ct") == (strlen($value) - 2)) {
			$seq = parse_ct_file("$dir/$value", $value);
			if ($seq)
				$ts->add_seq($seq);
			else
				$error_list[] = $error;
			unlink("$dir/$value");
		}
		// TODO: validate input!!
		if($value == 'info.txt') {
			$file_handle = fopen("$dir/$value", "r");
			if (!feof($file_handle)) {
				$ts->name = fgets($file_handle);
				if (!feof($file_handle))
					$ts->color = fgets($file_handle);
			}
			fclose($file_handle);
			unlink("$dir/$value");
		}
		if (file_exists("$dir/$value") && is_file("$dir/$value"))
			unlink("$dir/$value");
		/**/
	}
	//rmdir($dir);
	return $ts;
}

?>