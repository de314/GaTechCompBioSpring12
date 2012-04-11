var selectedList = 0;
var li_id_count = 0;
var ul_id_count = 0;
var trainingSets = new Object();


function addNewTrainingSet() {
	addTrainingSet2($("#newTitle").val(), new Array());
}

function addTrainingSet2(title, seqs) {
	$('.container')
			.append(
					"<div class='list' id='ul"
							+ ul_id_count
							+ "'><div id='title' class='title' onclick='selTitle(this)'><span id='name'>"
							+ title
							+ "</span>&nbsp;"+getDeleteIconString()+"</div><ul></ul></div></div>");
	var ts = new TrainingSet($("#ul" + ul_id_count), title);
	trainingSets["ul" + ul_id_count] = ts;
	ul_id_count++;
	for ( var i in seqs) {
		$('ul:last').append(
				"<li id='li" + li_id_count + "'>" + seqs[i].name + "</li>");
		ts.add(seqs[i]);
		seqs[i].ele = $("#li" + li_id_count);
		seqs[i].ele.attr("seq", seqs[i].seq);
		seqs[i].ele.attr("str", seqs[i].str);
		li_id_count++;
	}
	resetLists();
}

function getDeleteIconString() {
	return "<span style='color:red;float:right;background:#FFF;padding:0px 5px;border:1px solid #000' onclick='deleteTSet(this)'>X</span>";
}

function appendTrainingSet2(id, seqs) {
	var ts = trainingSets[id];
	for ( var i in seqs) {
		console.debug(seqs[i]);
		$('ul:last').append(
				"<li id='li" + li_id_count + "'>" + seqs[i].name + "</li>");
		ts.add(seqs[i]);
		seqs[i].ele = $("#" + id);
		seqs[i].ele.attr("seq", seqs[i].seq);
		seqs[i].ele.attr("str", seqs[i].str);
		li_id_count++;
	}
	resetLists();
}

function deleteTSet(ele) {
	delete trainingSets[$(ele.parentNode.parentNode).attr('id')];
	$(ele.parentNode.parentNode).remove();
	selectedList = 0;
}

function selTitle(ele) {
	if (selectedList)
		selectedList.css("border", "");
	if (ele.length == 0 || selectedList[0] == ele.parentNode) {
		selectedList = 0;
		return;
	}
	selectedList = $(ele.parentNode);
	selectedList.css("border", "2px solid #000");
}

function setColor(ele) {
	if (selectedList) {
		selectedList.css("background-color", $(ele).css("background-color"));
		trainingSets[selectedList.attr('id')].color = $(ele).css(
				"background-color");

	}
}


function setTitle() {
	if (selectedList) {
		var name = $("#newTitle").val();
		if (name && name != "") {
			$("#newTitle").val("");
			$(selectedList).find("#title #name").html(name);
			trainingSets[$(selectedList).attr("id")].name = name;
		}
	}
}

function upload_file() {
	var ele = $('#uploaded_file');
	var filename = ele.val();
	var url = "scripts/public_api.php";
	var func = 0;
	if (endsWith(filename, "ct")) {
		url += "?upload";
		func = receiveFile;
	} else if (endsWith(filename, ".zip")) {
		url += "?zip";
		func = receiveZip;
	}
	if (func)
		asynch_submit(url, func);
	else {
		alert("Cannot process file. '" + filename + "'. Files must end with *.ct or *.zip");
	}
	return false;
}

function endsWith(str, suffix) {
	return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function receiveFile(data, status) {
	var rna = handleSeq(data);
	if (rna) {
		if (selectedList && $(selectedList).length) {
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
	});
}

function train_grammars() {
	$("#resultsText").html("");
	var grammars = new Array();
	for (var ts in trainingSets) {
		var g = train_grammar(trainingSets[ts]);
		grammars.push(g);
		var exp = new Expectations(g);
		exp.calculate();
		var out = "<table style='border:2px solid "+trainingSets[ts].color+";'><tr><td><pre>" + g.output() + "</pre></td><td>";
		out += "<pre>" + g.output_counts() + "</pre></td><td>";
		out += "<pre>" + exp.output() + "</pre></td></tr></table>";
		$("#resultsText").append(out);
		trainingSets[ts].grammar = g;
	}
	generate_graphs();
	$("#tabs").tabs("select", 1);
}
