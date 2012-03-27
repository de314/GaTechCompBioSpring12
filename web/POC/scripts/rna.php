<?php 

class RNAobj {
	public $seq;
	public $str;
	public $filename;
	public function __construct($name, $sequence, $structure) { 
		$this->filename = $name;
		$this->seq = $sequence;
		$this->str = $structure;
	}
	public function write() {
		echo "Filename: " . $this->filename;
		echo "<br />&nbsp;&nbsp;&nbsp;Sequence: " . $this->seq;
		echo "<br />&nbsp;&nbsp;&nbsp;Structure: " . $this->str;
	}
	public function as_array() {
		return array("filename"=>$this->filename, "seq"=>$this->seq, "str"=>$this->str);
	}
	public function json() {
		echo json_encode($this->as_array());
	}
}

?>