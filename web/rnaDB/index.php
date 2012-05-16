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
					<a href="#"><div class="divLink topLink">Home</div></a>
					<a href="#"><div class="divLink">Search</div></a>
					<a href="#"><div class="divLink">Help</div></a>
					<a href="#"><div class="divLink botLink">More Information</div></a>
				</div>
				<!-- Content-->
				<div id="nav" style="float:left;padding:30px;width:750px;">
					<form style="margin-left:50px;">
						
						<!-- RNA Family -->
						<p class="formHeader">Select RNA Group(s):</p>
						<p id="rnaFamily" class="formItem">
							<input type="checkbox" id="familytRNA" name="familytRNA" checked="1" /><label for="familytRNA">tRNA</label>
							<input type="checkbox" id="family5S" name="family5S" checked="1" /><label for="family5S">5S rRNA</label>
							<input type="checkbox" id="family16S" name="family16S" checked="1" /><label for="family16S">16S rRNA</label>
							<input type="checkbox" id="family23S" name="family23S" checked="1" /><label for="family23S">23S rRNA</label>
						</p>
						
						<!-- Ambiguous -->
						<p class="formHeader">Allow ambiguous sequences.
							<input type="checkbox" id="ambiguous" name="ambiguous" 
								onclick="$('#ambLbl').find('span').html(this.checked?'Not Allowed':'Allowed');" />
							<label id="ambLbl" for="ambiguous">Allowed</label>
						</p>
						
						<!-- Alignable -->
						<p class="formHeader">Only select sequences in alignment
							<input type="checkbox" id="aligned" name="aligned" 
								onclick="$('#aliLbl').find('span').html(this.checked?'All Sequences':'Only In Alignment');" />
							<label id="aliLbl" for="aligned">Only In Alignment</label>
						</p>
						
						<!-- Sequence Length -->
						<p class="formHeader">Sequence Length:</p>
						<p id="rangeSeqLen" class="formItem"></p>
						
						<!-- Prediction Accuracy -->
						<p class="formHeader">MFE Prediction Accuracy:</p>
						<p id="rangePredAcc" class="formItem"></p>
						
						<!-- File Name -->
						<p class="formHeader">
							Sequence Name: <br />
							<span style="font-size:10;">(Note: all searches performed with "LIKE %...%")</span>
						</p>
						<p class="formItem">
							<label for="fileName">Name: </label>
							<input type="text" id="fileName" name="fileName" />
						</p>
						
						<!-- Accession Number -->
						<p class="formHeader">Accession Num</p>
						<p class="formItem">
							<label for="fileName">Acc. #: </label>
							<input type="text" id="fileName" name="fileName" />
						</p>
						
						<!-- Native Base Pair Density -->
						<p class="formHeader">Native Base Pair Density:</p>
						<p id="rangeNatBpDen" class="formItem"></p>
						
						<!-- Predicted Base Pair Density -->
						<p class="formHeader">Predicted Base Pair Density:</p>
						<p id="rangePredBpDen" class="formItem"></p>
						
						<!-- Stuffed Pair Density -->
						<p class="formHeader">Stuffed Pair Density:</p>
						<p id="rangeStuffedBpDen" class="formItem"></p> 
					</form>
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
						console.debug(ui);
						$("#min"+id).val(ui.values[ 0 ] / divider);
						$("#max"+id).val(ui.values[ 1 ] / divider);
					}
				}
			});
			// TODO: text boxes to set slider
			$('#min'+id).change(function() {
				console.debug(slider);
				slider.slide('values', [ this.value, slider.values[1]]);
			});
			$('#max'+id).change(function() {
				slider.slider('values', [ slider.values[0], this.value]);
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