/**
 * =================
 * 		SLIDERS
 * =================
 */
function setSlider(ele, max) {
	ele = $(ele);
	if ((/[0-9]+/).test(ele.val())) {
		//console.debug($("#"+ele.attr('sliderId')).slider());
		console.debug($("#"+ele.attr('sliderId')).slider);
		$("#"+ele.attr('sliderId')).slider('value', ele.val());
		ele.old = ele.value;
	} else {
		// TODO: reset to old/valid value
	}
}


function sliderRange(ele, id, min, max, divider) {
	ele.html('<label for="min'+id+'">Min: </label><input type="text" id="min'+id+'" sliderId="slider'+id+'" '+
		'name="min'+id+'" style="width:70px;" value="'+(min / divider)+'"" onblur="getSetSizeOut();" />'+
		'<span id="slider'+id+'" style="width:50%;display:inline-block;margin-left:20px;margin-right:20px;"></span>'+
		'<label for="max'+id+'">Max: </label><input type="text" id="max'+id+'" sliderId="slider'+id+'" '+
		'name="max'+id+'" style="width:70px;" value="'+(max / divider)+'"" onblur="getSetSizeOut();" />');
	var slider = $("#slider"+id).slider({
		range: true,
		min: min,
		max: max,
		values: [ min, max ],
		slide: function( event, ui ) {
			if (event && ui) {
				//console.debug(ui);
				$("#min"+id).val(ui.values[ 0 ] / divider);
				$("#max"+id).val(ui.values[ 1 ] / divider);
			}
		}
	});
	$("#slider"+id).live('blur', getSetSizeOut);
	// TODO: text boxes to set slider
	$('#min'+id).change(function() {
		var values = slider.slider( "option", "values" );
		values[0] = $("#min"+id).val() * divider;
		slider.slider( "option", "values", values );
	});
	$('#max'+id).change(function() {
		var values = slider.slider( "option", "values" );
		values[1] = $("#max"+id).val() * divider;
		slider.slider( "option", "values", values  );
	});
}

/**
 * ====================
 * 		SEARCH
 * ====================
 */
var currSizeId = 0;
function populateFormElement(jsonFormData) {
	jsonFormData.family = ($("#familytRNA").is(':checked') ? "tRna,":"")+
		($("#family5S").is(':checked') ? "5S,":"")+
		($("#family16S").is(':checked') ? "16S,":"")+
		($("#family23S").is(':checked') ? "23S,":"");
	if (jsonFormData.family.length > 0)
		jsonFormData.family = jsonFormData.family.substring(0,jsonFormData.family.length-1);
	jsonFormData.ambiguous = $("#ambiguous").is(':checked');
	jsonFormData.aligned = $("#aligned").is(':checked');
	jsonFormData.seqLength = $("#sliderSeqLen").slider( "option", "values" );
	jsonFormData.mfeAccuracy = $.extend(true, [], $("#sliderPredAcc").slider( "option", "values" ));
	jsonFormData.mfeAccuracy[0] = jsonFormData.mfeAccuracy[0]/1000;
	jsonFormData.mfeAccuracy[1] = jsonFormData.mfeAccuracy[1]/1000;
	jsonFormData.name = $("#fileName").val();
	jsonFormData.natDensity = $.extend(true, [], $("#sliderNatBpDen").slider( "option", "values" ));
	jsonFormData.natDensity[0] = jsonFormData.natDensity[0]/1000;
	jsonFormData.natDensity[1] = jsonFormData.natDensity[1]/1000;
	jsonFormData.predDensity = $.extend(true, [], $("#sliderPredBpDen").slider( "option", "values" ));
	jsonFormData.predDensity[0] = jsonFormData.predDensity[0]/1000;
	jsonFormData.predDensity[1] = jsonFormData.predDensity[1]/1000;
	jsonFormData.stuffedDensity = $.extend(true, [], $("#sliderStuffedBpDen").slider( "option", "values" ));
	jsonFormData.stuffedDensity[0] = jsonFormData.stuffedDensity[0]/1000;
	jsonFormData.stuffedDensity[1] = jsonFormData.stuffedDensity[1]/1000;
}
function updateConfirmText(jsonFormData) { 
	$("#confirmFamily").html(jsonFormData.family);
	$("#confirmAmbiguous").html(jsonFormData.ambiguous ? "Allowed" : "Not Allowed");
	$("#confirmAligned").html(jsonFormData.aligned ? "Required" : "Not Required");
	$("#confirmLenMin").html(jsonFormData.seqLength[0]);
	$("#confirmLenMax").html(jsonFormData.seqLength[1]);
	$("#confirmMfeAccMin").html(jsonFormData.mfeAccuracy[0]);
	$("#confirmMfeAccMax").html(jsonFormData.mfeAccuracy[1]);
	$("#confirmName").html(jsonFormData.name);
	$("#confirmNatDenMin").html(jsonFormData.natDensity[0]);
	$("#confirmNatDenMax").html(jsonFormData.natDensity[1]);
	$("#confirmPredDenMin").html(jsonFormData.predDensity[0]);
	$("#confirmPredDenMax").html(jsonFormData.predDensity[1]);
	$("#confirmStuffedDenMin").html(jsonFormData.stuffedDensity[0]);
	$("#confirmStuffedDenMax").html(jsonFormData.stuffedDensity[1]);
}
function getSetSizeOut() {
	currSizeId++;
	var jsonFormData = {};
	jsonFormData.sizeId = currSizeId;
	populateFormElement(jsonFormData);
	updateConfirmText(jsonFormData);
	$.ajax({
		type: 'POST',
		url: "scripts/rnadb_api.php?getSize",
		data: jsonFormData,
		success: getSetSizeIn
	});
}
function getSetSizeIn(data) {
	console.debug(data);
	var obj = JSON.parse(data);
	if (obj.setId == currSizeId) {
		$("#sizeBox").html("");
		$("<span>Set Size: "+obj.setSize+"</span>").hide().appendTo("#sizeBox").fadeIn(2000);
	}
}
function submitSearch() {
	var jsonFormData = {};
	populateFormElement(jsonFormData);
	$('<form id="searchForm" method="POST" action="results.php"></form>').appendTo('body');
	//$('<form id="searchForm" method="POST" action="scripts/rnadb_api.php?getSize"></form>').appendTo('body');
	$('<input>').attr({ type: 'hidden', name: 'family', value: jsonFormData.family }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'ambiguous', value: jsonFormData.ambiguous }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'aligned', value: jsonFormData.aligned }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'lenmin', value: jsonFormData.seqLength[0] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'lenmax', value: jsonFormData.seqLength[1] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'mfeaccmin', value: jsonFormData.mfeAccuracy[0] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'mfeaccmax', value: jsonFormData.mfeAccuracy[1] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'name', value: jsonFormData.name }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'natdenmin', value: jsonFormData.natDensity[0] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'natdenmax', value: jsonFormData.natDensity[1] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'preddenmin', value: jsonFormData.predDensity[0] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'preddenmax', value: jsonFormData.predDensity[1] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'stuffeddenmin', value: jsonFormData.stuffedDensity[0] }).appendTo('#searchForm');
	$('<input>').attr({ type: 'hidden', name: 'stuffeddenmax', value: jsonFormData.stuffedDensity[1] }).appendTo('#searchForm');
	return $("#searchForm").submit();
}


/**
 * ====================
 * 		SEARCH
 * ====================
 */
var offset = 0;
var setSize = 100;
var allDownloaded = 0;
var appendTime = 0;
function changeAllCheckboxes() {
	 $("INPUT[type='checkbox']").attr('checked', $('#cbAll').is(':checked'));
}
function downloadOut(set) {
	if (set=='all' && allDownloaded) {
		alert("All sequences have already been downloaded:\n" + allDownloaded);
		return;
	}
	if (set == 'all') {
		allDownloaded = 1;
		$.ajax({
			type: 'POST',
			url: "scripts/rnadb_api.php?downloadAll",
			data: searchParams,
			success:downloadIn
		});
	} else {
		var selected = "";
		$("INPUT[type='checkbox']").each(function() {
			var ele = $(this);
			if ((set=='curr' | ele.attr('checked')=='checked') && ele.attr('rnaId')) {
				selected += ele.attr('rnaId') + ",";
			}
		});
		if (selected.length > 0)
			selected = selected.substring(0, selected.length-1);
		var jsonData = {};
		jsonData.selected = selected;
		$.ajax({
			type: 'POST',
			url: "scripts/rnadb_api.php?download",
			data: jsonData,
			success:downloadIn
		});
	}
	$("#downloadLinksLoading").html("<img src='images/loading_small.gif' />");
}
function downloadIn(data) {
	console.debug(data);
	$("#downloadLinksLoading").html("");
	if (!appendTime) {
		$("#downloadLinks").html("");
		appendTime = 1;
	}
	var obj = JSON.parse(data);
	if (obj.link) {
		if (allDownloaded == 1)
			allDownloaded = obj.link;
		$("<p>Download Zipped File: <a href='" + obj.link + "'>" + obj.link + "</a></p>")
			.hide()
			.appendTo("#downloadLinks")
			.fadeIn(2000);
	} else {
		$("<p style='color:red;'>Download Error: " + obj.error + "</p>")
			.hide()
			.appendTo("#downloadLinks")
			.fadeIn(2000);
	}
}
function searchOut(newOffset) {
	searchParams.offset = newOffset;
	$.ajax({
		type: 'POST',
		url: "scripts/rnadb_api.php?search",
		data: searchParams,
		success:searchIn
	});
	$("#setSelectionLinks").html("<img src='images/loading_small.gif' />");
}
function searchIn(data) {
	var obj = JSON.parse(data);
	if (obj) {
		offset = obj.offset;
		initTable();
		var ele = $("#rnaTable");
		for (var i in obj.rows) {
			var arr = obj.rows[i];
			ele.append('<tr><td><input type="checkbox" rnaId='+arr.rid+
					' /></td><td>'+arr.name+'</td>'+
					'<td>'+arr.family+'</td>'+
					'<td>'+arr.seqLength+'</td>'+
					'<td>'+arr.mfeAcc+'</td>'+
					'<td>'+arr.natDensity+'</td>'+
					'<td>'+arr.predDensity+'</td>'+
					'<td>'+arr.stuffedDensity+'</td>'+
					'<td>'+(arr.ambiguous?"Yes":"No")+'</td>'+
					'<td>'+(arr.alignment?"Yes":"No")+'</td></tr>');
		} 
	}
	populateSetSelectionLinks();
}
function populateSetSelectionLinks() {
	populateSetSelectionLinks2($("#setSelectionLinks1"));
	populateSetSelectionLinks2($("#setSelectionLinks2"));
}
function populateSetSelectionLinks2(ele) {
	ele.html("");
	var i = offset > 0 ? offset - setSize : offset;
	if (i != 0) {
		var str = "<span>...&nbsp;&nbsp;::&nbsp;&nbsp;</span>";
		ele.append($(str));
	}
	var count = 0;
	for(;(i+setSize)<max && count < 6;i+=setSize, count++) {
		var str = "<span>";
		if (i != offset)
			str = "<a href='#' onclick='searchOut("+i+");return false;'>";
		str += i+"-"+(i+setSize-1);
		if (i != offset)
			str += "</a>";
		str += "&nbsp;&nbsp;::&nbsp;&nbsp;</span>";
		ele.append($(str));
	}
	if (count == 6) {
		var str = "<span>...&nbsp;&nbsp;::&nbsp;&nbsp;</span>";
		ele.append($(str));
	}
	i = Math.floor(max / setSize) * setSize;
	var str = "<span>";
	if (i != offset)
		str = "<a href='#' onclick='searchOut("+i+");return false;'>";
	str += i+"-"+max;
	if (i != offset)
		str += "</a>";
	str += "</span>";
	ele.append($(str));
}
function initTable() {
	$("#rnaTable").html('<tr><th><input id="cbAll" type="checkbox" onclick="changeAllCheckboxes();" /></th><th>Name</th>'+
	'<th>Family</th><th>Seq. Length</th><th>MFE Acc.</th><th>Native Density</th>'+
	'<th>Predicted Density</th><th>Stuffed Density</th><th>Ambiguous</th><th>Aligned</th></tr>');
}