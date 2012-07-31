<?php
session_start();
// TODO: Check that user is logged in through CAS else redirect

?>

<html>
<head>
	<title>Georgia Institute of Technology RNA Database</title>
	<link type="text/css" href="css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript">
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
			jsonFormData.mfeAccuracy = $("#sliderPredAcc").slider( "option", "values" );
			jsonFormData.name = $("#fileName").val();
			jsonFormData.accession = $("#accessionNum").val();
			jsonFormData.natDensity = $("#sliderNatBpDen").slider( "option", "values" );
			jsonFormData.predDensity = $("#sliderPredBpDen").slider( "option", "values" );
			jsonFormData.stuffedDensity = $("#sliderStuffedBpDen").slider( "option", "values" );
		}
		function updateConfirmText(jsonFormData) { 
			$("#confirmFamily").html(jsonFormData.family);
			$("#confirmAmbiguous").html(jsonFormData.ambiguous ? "Allowed" : "Not Allowed");
			$("#confirmAligned").html(jsonFormData.aligned ? "Required" : "Not Required");
			$("#confirmLenMin").html(jsonFormData.seqLength[0]);
			$("#confirmLenMax").html(jsonFormData.seqLength[1]);
			$("#confirmMfeAccMin").html(jsonFormData.mfeAccuracy[0] / 1000);
			$("#confirmMfeAccMax").html(jsonFormData.mfeAccuracy[1] / 1000);
			$("#confirmName").html(jsonFormData.name);
			$("#confirmAccession").html(jsonFormData.accession);
			$("#confirmNatDenMin").html(jsonFormData.natDensity[0] / 1000);
			$("#confirmNatDenMax").html(jsonFormData.natDensity[1] / 1000);
			$("#confirmPredDenMin").html(jsonFormData.predDensity[0] / 1000);
			$("#confirmPredDenMax").html(jsonFormData.predDensity[1] / 1000);
			$("#confirmStuffedDenMin").html(jsonFormData.stuffedDensity[0] / 1000);
			$("#confirmStuffedDenMax").html(jsonFormData.stuffedDensity[1] / 1000);
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
			var obj = eval('(' + data + ')');
			if (obj.setId == currSizeId) {
				$("#sizeBox").html("&nbsp;");
				$("<span>Set Size: "+obj.setSize+"</span>").hide().appendTo("#sizeBox").fadeIn(2000);
			}
		}
		function submitSearch() {
			var jsonFormData = {};
			populateFormElement(jsonFormData);
			$('<form id="searchForm" method="POST" action="results.php"></form>').appendTo('body');
			$('<input>').attr({ type: 'hidden', name: 'family', value: jsonFormData.family }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'ambiguous', value: jsonFormData.ambiguous }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'aligned', value: jsonFormData.aligned }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'lenmin', value: jsonFormData.seqLength[0] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'lenmax', value: jsonFormData.seqLength[1] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'mfeaccmin', value: jsonFormData.mfeAccuracy[0] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'mfeaccmax', value: jsonFormData.mfeAccuracy[1] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'name', value: jsonFormData.name }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'accession', value: jsonFormData.accession }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'natdenmin', value: jsonFormData.natDensity[0] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'natdenmax', value: jsonFormData.natDensity[1] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'preddenmin', value: jsonFormData.predDensity[0] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'preddenmax', value: jsonFormData.predDensity[1] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'stuffeddenmin', value: jsonFormData.stuffedDensity[0] }).appendTo('#searchForm');
			$('<input>').attr({ type: 'hidden', name: 'stuffeddenmax', value: jsonFormData.stuffedDensity[1] }).appendTo('#searchForm');
			return $("#searchForm").submit();
		}
	</script>
	<style type="text/css">
		.divLink { 
			text-align:center;
			font-weight: bold;
			background: #e0ffc7;
			border: 1px solid #000;
			width: 100%;
			padding: 15px;
			color: #000;
		}
		.sizeBox {
			position : fixed;
			top      : 100px;
			right    : 30px;
			border	 : 1px solid black;
			padding	 : 10px;
		}
		.topLink {
			margin-top: 50px;
			border-radius: 8px 8px 0px 0px;
		}
		.botLink {
			border-radius: 0px 0px 8px  8px;
		}
		.divLink:Hover {
			background: #a4d47d;
		}
		.formItem {
			margin-left: 50px;
		}
		.formHeader {
		 	font-weight: bold;
		 	padding-top: 25px;
		}
		.confirmBox {
			padding: 15px;
			width: 600px;
			align:center;
		}
	</style>
</head>
<body>
	<div id="container">
		<!-- Tabs -->
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Georgia Institute of Technology RNA Database</a></li>
			</ul>
			<div id="tabs-1" style="height:auto;">
				<!-- Navigation -->
				<div id="nav" style="float:left;width:180px;padding-right:15px">
					<a href="index.php"><div class="divLink topLink">Home</div></a>
					<a href="search.php"><div class="divLink">Search</div></a>
					<a href="help.php"><div class="divLink">Help</div></a>
					<a href="help.php?moreInfo"><div class="divLink botLink">More Information</div></a>
				</div>
				<!-- Content-->
				<div id="nav" style="float:left;padding:30px;width:750px;">
						More Coming Soon...
				</div>	
			</div>
		</div>
	</div>
	<script type="text/javascript">
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
				'name="min'+id+'" style="width:70px;" value="'+(min / divider)+'"" />'+
				'<span id="slider'+id+'" style="width:50%;display:inline-block;margin-left:20px;margin-right:20px;"></span>'+
				'<label for="max'+id+'">Max: </label><input type="text" id="max'+id+'" sliderId="slider'+id+'" '+
				'name="max'+id+'" style="width:70px;" value="'+(max / divider)+'"" />');
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
		$(document).ready(function() {
			$('#tabs').tabs();
			sliderRange($('#rangeSeqLen'),'SeqLen',0,3000, 1);
			sliderRange($('#rangeNatBpDen'),'NatBpDen',0,1000, 1000);
			sliderRange($('#rangePredBpDen'),'PredBpDen',0,1000, 1000);
			sliderRange($('#rangeStuffedBpDen'),'StuffedBpDen',0,1000, 1000);
			sliderRange($('#rangePredAcc'),'PredAcc',0,1000, 1000);
			$( "#ambiguous" ).button();
			$( "#aligned" ).button();
			$( "#family" ).button();
			$( "#rnaFamily" ).buttonset();
		});
		$(window).load(function() {
			$('#tabs-1').append('<div id="bottomClearDiv" style="clear:both;" class="clear"></div>');
		});
		// TODO: validate sequence length and set slider
	</script>
</body>
</html>