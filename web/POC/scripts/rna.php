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

class TrainingSet {
	public $name;
	public $color;
	public $seqs;
	public $errors;
	public function __construct($name, $color, $sequences) {
		$this->name = $name;
		$this->color = $color ? $color : "#FFF";
		$this->seqs = $sequences ? $sequences : array();
		$this->errors = array();
	}
	public function add_seq($rna) {
		if ($rna)
			$this->seqs[] = $rna;
	}
	public function add_error($err) {
		if ($err)
			$this->errors[] = $err;
	}
	public function as_array() {
		$seqs_arr = array();
		foreach($seqs as $rna)
			$seqs_arr[] = $rna->as_array();
		return array("name"=>$this->name, "seqs"=>$seqs_arr, "color"=>$this->color, "errors"=>$this->errors);
	}
	public function json() {
		echo json_encode($this->as_array());
	}
}

?>