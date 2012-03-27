function TrainingSet(ele, name, seqs, color) {
	this.ele = ele;
	this.name = name;
	this.sequences = seqs ? seqs : {};
	this.color = color ? color : '#FFF';
	this.add = function(seq) {
		console.debug(seq);
		if (seq && $(seq.ele).attr('id') && seq.seq && seq.str)
			this.sequences[$(seq.ele).attr('id')] = seq;
	}
	this.remove = function(id) {
		if (this.sequences[id]) {
			delete sequences[id];
			resetLists();
		}
	}
	this.clear = function() {
		this.sequences = {};
	}
}

function Sequence(ele, name, seq, str) {
	this.ele = ele;
	this.name = name;
	this.seq = seq;
	this.str = str;
	this.output = function() {
		return this.name + ": " + this.seq + " - " + this.str;
	}
}

function upload_file() {
	var ele = $('#uploaded_file');
	var filename = ele.val();
	var url = "scripts/public_api.php";
	var func = 0;
	if (endsWith(filename, ".ct")) {
		url += "?upload";
		func = receiveFile;
	} else if (endsWith(filename, ".zip")) {
		url += "?zip";
		func = receiveZip;
	}
	if (func)
		asynch_submit(url, func);
	else {
		// TODO: Invalid file format
	}
	return false;
}
function endsWith(str, suffix) {
	return str.indexOf(suffix, str.length - suffix.length) !== -1;
}
function receiveFile(data, status) {
	var rna = handleSeq(data);
	if (rna) {
		console.debug(rna);
		if (selectedList && $(selectedList).length) {
			console.debug(selectedList);
			var arr = new Array();
			arr[0] = rna;
			appendTrainingSet2(selectedList.attr("id"), arr);
		} else {
			var seqs = new Array();
			seqs[0] = rna;
			addTrainingSet2("New Training Set", seqs);
		}
	}
}
function handleSeq(obj) {
	return new Sequence(0, obj.filename, obj.seq, obj.str);
}
function receiveZip(data, status) {
	var i;
	var rnas = new Array();
	for (i = 0; i < data.length; i++)
		rnas.push(handleSeq(data[i]));
	if (rnas.length > 0) {
		var name = prompt("Training set uploaded successfully. Please enter training set name:", "New Training Set");
		if (name != null && name != "")
			addTrainingSet2(name, rnas);
	}
}

function asynch_submit(target_url, return_func) {
	/*
	 * prepareing ajax file upload url: the url of script file handling the
	 * uploaded files fileElementId: the file type of input element id and it
	 * will be the index of $_FILES Array() dataType: it support json, xml
	 * secureuri:use secure protocol success: call back function when the ajax
	 * complete error: callback function when the ajax failed
	 */
	$.ajaxFileUpload({
		url : target_url,
		secureuri : false,
		fileElementId : 'uploaded_file',
		dataType : 'json',
		success : return_func,
		error : function(data, status, e) {
			console.debug(data);
			alert(e);
		}
	})
}
