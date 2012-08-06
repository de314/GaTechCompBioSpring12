<?php
session_start();

?>

<html>
<head>
	<title>Georgia Institute of Technology RNA Database</title>
	<link type="text/css" href="css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
	<link type="text/css" href="css/main.css" rel="stylesheet" />
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript" src="js/rnaDB.min.js"></script>
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
				<!-- Size Box -->
				<div id="sizeBox" class="sizeBox">Set Size: 0</div>
				<!-- Content-->
				<div id="nav" style="float:left;padding:30px;width:750px;">
						
						<!-- RNA Family -->
						<p class="formHeader">Select RNA Group(s):</p>
						<p id="rnaFamily" class="formItem">
							<input type="checkbox" id="familytRNA" name="familytRNA" checked="1" onchange="getSetSizeOut();" /><label for="familytRNA">tRNA</label>
							<input type="checkbox" id="family5S" name="family5S" checked="1" onchange="getSetSizeOut();" /><label for="family5S">5S rRNA</label>
							<input type="checkbox" id="family16S" name="family16S" checked="1" onchange="getSetSizeOut();" /><label for="family16S">16S rRNA</label>
							<input type="checkbox" id="family23S" name="family23S" checked="1" onchange="getSetSizeOut();" /><label for="family23S">23S rRNA</label>
						</p>
						
						<!-- Ambiguous -->
						<p class="formHeader">Allow ambiguous sequences.
							<input type="checkbox" id="ambiguous" name="ambiguous" checked="1" 
								onclick="$('#ambLbl').find('span').html(this.checked?'Allowed':'Not Allowed');getSetSizeOut();" />
							<label id="ambLbl" for="ambiguous">Allowed</label>
						</p>
						
						<!-- Alignable -->
						<p class="formHeader">Only select sequences in alignment
							<input type="checkbox" id="aligned" name="aligned" checked="1"
								onclick="$('#aliLbl').find('span').html(this.checked?'Only In Alignment':'All Sequences');getSetSizeOut();" />
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
							<label for="fileName">File Name: </label>
							<input type="text" id="fileName" name="fileName" onblur="getSetSizeOut();" />
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
				</div>
				<!-- Confirm Box -->
				<div class="confirmBox" id="confirmBox">
					&nbsp;<br/>
					&nbsp;<br/>
					Confirm the following search criteria then submit the search below.<br />
					&nbsp;<br/>
					<b>Family:</b>&nbsp;&nbsp;{<span id="confirmFamily">tRna,5S,16S,23S</span>}<br />
					<b>Ambiguous:</b>&nbsp;&nbsp;<span id="confirmAmbiguous">Allowed</span><br />
					<b>Aligned:</b>&nbsp;&nbsp;<span id="confirmAligned">Required</span><br />
					<b>Sequence Length:</b>&nbsp;&nbsp;<span id="confirmLenMin">0</span>-<span id="confirmLenMax">3000</span><br />
					<b>MFE Accuracy:</b>&nbsp;&nbsp;<span id="confirmMfeAccMin">0</span>-<span id="confirmMfeAccMax">1</span><br />
					<b>File Name:</b>&nbsp;&nbsp;"<span id="confirmName"></span>"<br />
					<b>Natural Density:</b>&nbsp;&nbsp;<span id="confirmNatDenMin">0</span>-<span id="confirmNatDenMax">1</span><br />
					<b>Predicted Density:</b>&nbsp;&nbsp;<span id="confirmPredDenMin">0</span>-<span id="confirmPredDenMax">1</span><br />
					<b>Stuffed Density:</b>&nbsp;&nbsp;<span id="confirmStuffedDenMin">0</span>-<span id="confirmStuffedDenMax">1</span><br />
				</div>
				<!-- Submit Box -->
				<div class="submitBox" id="submitBox">
					<button onclick="submitSearch();">Submit Search</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#tabs').tabs();
			sliderRange($('#rangeSeqLen'),'SeqLen',0,3000, 1);
			sliderRange($('#rangeNatBpDen'),'NatBpDen',0,1000, 1000);
			sliderRange($('#rangePredBpDen'),'PredBpDen',0,1000, 1000);
			sliderRange($('#rangeStuffedBpDen'),'StuffedBpDen',0,2000, 1000);
			sliderRange($('#rangePredAcc'),'PredAcc',0,1000, 1000);
			$( "#ambiguous" ).button();
			$( "#aligned" ).button();
			$( "#family" ).button();
			$( "#rnaFamily" ).buttonset();
		});
		$(window).load(function() {
			$('#tabs-1').append('<div id="bottomClearDiv" style="clear:both;" class="clear"></div>');
			getSetSizeOut();
		});
	</script>
</body>
</html>