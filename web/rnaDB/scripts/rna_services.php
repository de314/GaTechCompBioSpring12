<?php 

include_once "rnadb.php";

/* creates a compressed zip file */
function create_zip($filenames = array()) {
	$file = fopen("count", "r") or exit("");
	$count = intval(fgets($file));
	fclose($file);
	$file = fopen("count", "w") or exit("");
	fwrite($file, ++$count);
	fclose($file);
	$destination = "downloads/" . time() . '_' . ($count) . '.zip';
	//vars
	$valid_files = array();
	//if files were passed in...
	if(is_array($filenames)) {
		//cycle through each file
		foreach($filenames as $file) {
			if (file_exists("../".$file))
				$valid_files[] = $file;
		}
	}
	//if we have good files...
	if(count($valid_files)) {
		//create the archive
		$zip = new ZipArchive();
		if($zip->open("../".$destination,ZIPARCHIVE::CREATE) !== true) {
			return "Server Error: could not open zip file.";
		}
		
// 		echo "created: ../".$destination,"\n";
		
		//add the files
		foreach($valid_files as $file) {
			$zip->addFile("../".$file,basename($file));
// 			echo "../".$file,", ",basename($file)," exists(",file_exists("../".$file),")\n";
		}
		//debug
// 		echo "\nnumFiles: ",$zip->numFiles,' status: ',$zip->status, ' filename: *',$zip->filename, "* cwd:",getcwd(),"\n";

		//close the zip -- done!
		$zip->close();

		//check to make sure the file exists
		if (file_exists("../".$destination))
			return $destination;
		return "Server Error: failed to write zip file.";
	}
	else
	{
		return "Server Error: files do not exist.";
	}
}

function addDirectory($dirName) {
	$dirs = array();
	$dh  = opendir($dirName);
	while (false !== ($filename = readdir($dh))) {
		if (is_dir($filename))
			$dirs[] = $filename;
		elseif (endsWith($filename, ".dbentry"))
			insertStructure(parseDbFile("../db/".$filename));
	}
	// TODO: handle recursive dirs
	closedir($dh);
}


// expecting: { filename,
//				seqlen,
//				ambiguous,
//				alignment,
//				family,
//				den }
//expecting: {  technique, 
//				predname, 
//				acc, 
//				pden, 
//				sden }
function parseDbFile($dbFilename) {
	$rna = array();
 	$file = fopen($dbFilename, "r") or exit("Unable to open file!");
	$arr = explode(":", fgets($file));
	$rna["family"] = trim($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["ambiguous"] = intval($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["alignment"] = intval($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["seqlen"] = intval($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["filename"] = trim($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["den"] = floatval($arr[1]);
	
	$arr = explode(":", fgets($file));
	$rna["technique"] = trim($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["predname"] = trim($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["acc"] = floatval($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["pden"] = floatval($arr[1]);
	$arr = explode(":", fgets($file));
	$rna["sden"] = floatval($arr[1]);
	fclose($file);
	return $rna;
}

function endsWith($haystack,$needle,$case=true)
{
	$expectedPosition = strlen($haystack) - strlen($needle);

	if($case)
		return strrpos($haystack, $needle, 0) === $expectedPosition;

	return strripos($haystack, $needle, 0) === $expectedPosition;
}

?>