var selectedList = 0;
var li_id_count = 0;
var ul_id_count = 0;
var trainingSets = new Object();
var csvStatus = 0;
var currently_deleting = 0;


function addNewTrainingSet() {
	addTrainingSet2($("#newTitle").val(), new Array());
}

function addTrainingSet2(title, seqs) {
	addTrainingSet3(title, seqs, "0xFFF");
}

function addTrainingSet3(title, seqs, color) {
	$('.container')
			.append(
					"<div class='list' style='background:"+color+";' id='ul"
							+ ul_id_count
							+ "'><div id='title' class='title' onclick='selTitle(this)'><span id='name'>"
							+ title
							+ "</span>&nbsp;"+getDeleteIconString()+"</div><ul></ul></div></div>");
	var ts = new TrainingSet($("#ul" + ul_id_count), title, 0, color);
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
	//style="clear:both;"
	$("#bottomClearDiv").remove();
	$('.container').append('<div id="bottomClearDiv" style="clear:both;" class="clear">&nbsp;<br /></div>');
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
	//style="clear:both;"
	$("#bottomClearDiv").remove();
	$('.container').append('<div id="bottomClearDiv" style="clear:left;" class="clear"></div>');
}

function deleteTSet(ele) {
	$('#titleSet').hide();
	$('#colorSet').hide();
	if (selectedList)
		selectedList.css("border", "");
	selectedList = 0;
	delete trainingSets[$(ele.parentNode.parentNode).attr('id')];
	$(ele.parentNode.parentNode).remove();
	currently_deleting = 1;
}

function selTitle(ele) {
	if (currently_deleting) {
		currently_deleting = 0;
		return;
	}
	if (selectedList)
		selectedList.css("border", "");
	else {
		$('#titleSet').show();
		$('#colorSet').show();
	}
	if (ele.length == 0 || selectedList[0] == ele.parentNode) {
		selectedList = 0;
		$('#titleSet').hide();
		$('#colorSet').hide();
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
	if (func) {
		$('#buttonUpload').hide();
		asynch_submit(url, func);
	} else {
		alert("Cannot process file. '" + filename + "'. Files must end with *.ct or *.zip");
	}
	return false;
}

function endsWith(str, suffix) {
	return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function receiveFile(data, status) {
	if (data.errors && data.errors.length > 0) {
		var err = "Errors:\n";
		for (var i=0;i<data.errors.length;i++)
			err += data.errors[i] + "\n";
		alert(err);
		return;
	}
	var rna = handleSeq(data);
	if (rna) {
		var seqs = new Array();
		seqs[0] = rna;
		// TODO: training set name
		addTrainingSet2("New Training Set", seqs);
	}
}

function handleSeq(obj) {
	return new Sequence(0, obj.filename, obj.seq, obj.str);
}

function handleTS(obj) {
	var rnas = new Array();
	for (i = 0; i < obj.seqs.length; i++) {
		rnas.push(handleSeq(obj.seqs[i]));
	}
	return rnas;
}

/*
function receiveZip(data, status) {
	var i;
	var rnas = new Array();
	// TODO: taining set name(s)
	for (i = 0; i < data.length; i++)
		rnas.push(handleSeq(data[i]));
	if (rnas.length > 0) {
		var name = prompt("Training set uploaded successfully. Please enter training set name:", "New Training Set");
		if (name != null && name != "")
			addTrainingSet2(name, rnas);
	}
}
/**/

function receiveZip(data, status) {
	var i;
	var ts_collection = new Array();
	for (i = 0; i < data.length; i++) {
		if (data[i].errors && data[i].errors.length > 0)
			handleErrors(data[i]);
		else
			addTrainingSet3(data[i].name, handleTS(data[i]), data[i].color);
	}
}

function handleErrors(ts) {
	if (ts.errors && ts.errors.length > 0) {
		var err = "Errors:\n";
		for (var i=0;i<ts.errors.length;i++)
			err += ts.errors[i] + "\n";
		alert(err);
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
	showFormatted();
	$("#tabs").tabs("select", 2);
}

function getHeading() {
	return "<a href='#' onClick='showFormatted();'>View Formatted Text</a>&nbsp;&nbsp;" + 
	"<a href='#' onClick='showBar();'>View Bar Graphs</a>&nbsp;&nbsp;" +
	"<a href='#' onClick='showCSV();'>View CSV Output</a>&nbsp;&nbsp;"+
	'<p><b>NOTE:</b> "NaN" means "Not a Number". This usually means that there are no sequences in the training set.<\p>'+
	"<hr /><br />";
}

function showFormatted() {
	$("#resultsText").html(getHeading());
	var out = "";
	for (var ts in trainingSets) {
		var g = trainingSets[ts].grammar;
		if (!g) {
			g = train_grammar(trainingSets[ts]);
			trainingSets[ts].grammar = g;
		}
		var exp = new Expectations(g);
		exp.calculate();
		out += "<table style='border:2px solid "+trainingSets[ts].color+";'><tr><td><pre>" + g.output() + "</pre></td><td>";
		out += "<pre>" + g.output_counts() + "</pre></td><td>";
		out += "<pre>" + exp.output() + "</pre></td></tr></table>";
	}
	$("#resultsText").append(out);
}

function showCSV() {
	$("#resultsText").html(getHeading());
	var out = "<textarea cols='80' rows='20'>";
	for (var ts in trainingSets) {
		var g = trainingSets[ts].grammar;
		if (!g) {
			g = train_grammar(trainingSets[ts]);
			trainingSets[ts].grammar = g;
		}
		out +=  g.csv_counts() + "\n\n\n";
	}
	$("#resultsText").append(out).append("</textarea>");
}

function showBar() {
	$("#resultsText").html(getHeading());
	$("#resultsText").append("<div id='barGraph'></div>");
	for (var ts in trainingSets) {
		var g = trainingSets[ts].grammar;
		if (!g) {
			g = train_grammar(trainingSets[ts]);
			trainingSets[ts].grammar = g;
		}
	}
	generate_graphs();
}

