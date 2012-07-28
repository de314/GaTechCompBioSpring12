<?php
session_start();
// TODO: Check that user is logged in through CAS else redirect

include_once 'scripts/rnadb_api.php';

function getRnaHtml($rna) {
	return '<tr><td><input type="checkbox" rnaId='.$rna['rid'].' /></td><td>
		<a href="#" onclick="alert(\'Display Info\');">'.$rna['name'].'</a></td>
		<td>'.$rna["accession"].'</td>
		<td>'.$rna["family"].'</td>
		<td>'.$rna["seqLength"].'</td>
		<td>'.$rna["mfeAcc"].'</td>
		<td>'.$rna["natDensity"].'</td>
		<td>'.$rna["predDensity"].'</td>
		<td>'.$rna["stuffedDensity"].'</td>
		<td>'.($rna["ambiguous"]?"Yes":"No").'</td>
		<td><a href="#" onclick="alert(\''.$rna["alignment"].'\');">View</a></td></tr>';
}

function populateTable() {
	$params = array(); // TODO
	$arr = getSequences($params);
	for($i=0;$i<count($arr);$i++)
		echo getRnaHtml($arr[$i]);
}

?>

<html>
<head>
	<title>Georgia Institute of Technology RNA Database</title>
	<link type="text/css" href="css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript">
		function changeAllCheckboxes() {
			 $("INPUT[type='checkbox']").attr('checked', $('#cbAll').is(':checked'));
		}
		function downloadAllOut() {
			$.ajax({
				url: "scripts/rnadb_api.php?downloadAll",
				context: document.body,
				success: downloadAllIn 
			});
		}
		function downloadAllIn(data) {
			$("#downloadLinks").html("");
			var obj = eval('(' + data + ')');
			$("<span>Download Zipped File: <a href='" + obj.link + "'>" + obj.link + "</a></span>")
				.hide()
				.appendTo("#downloadLinks")
				.fadeIn(2000);
		}
		function downloadSelectedOut() {
			var selected = "";
			$("INPUT[type='checkbox']").each(function() {
				var ele = $(this);
				if (ele.attr('checked') && ele.attr('rnaId'))
					selected += ele.attr('rnaId') + ",";
			});
			if (selected.length > 0)
				selected = selected.substring(0, selected.length-1);
			$.ajax({
				url: "scripts/rnadb_api.php?downloadSelected&selected=1,2,3,4",
				context: document.body,
				success:downloadSelectedIn 
			});
		}
		function downloadSelectedIn(data) {
			$("#downloadLinks").html("");
			var obj = eval('(' + data + ')');
			$("<span>Download Zipped File: <a href='" + obj.link + "'>" + obj.link + "</a></span>")
				.hide()
				.appendTo("#downloadLinks")
				.fadeIn(2000);
		}
	</script>
	<style type="text/css">
		.navDiv {
			float:left;
			width:180px;
			padding:15px;
		}
		.leftMain {
			float:left;
			width:800px;
			padding:10px;
			padding-left:25px;
		}
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
				<div id="nav" class="navDiv">
					<a href="#"><div class="divLink topLink">Home</div></a>
					<a href="#"><div class="divLink">Search</div></a>
					<a href="#"><div class="divLink">Help</div></a>
					<a href="#"><div class="divLink botLink">More Information</div></a>
				</div>
				<!-- Content-->
				<div id="downloadButtons" class="leftMain">
					<button onclick="downloadAllOut();">Download All Sequences</button>
					<button onclick="downloadSelectedOut();">Download Selected Sequences</button>
				</div>
				<div id="downloadLinks" class="leftMain">
					<i>Download links will appear here.</i>
				</div>
				<div id="sequenceGrid" class="leftMain">
					<table border="1px"; id="rnaTable">
						<tr>
							<th><input id="cbAll" type="checkbox" onclick="changeAllCheckboxes();" /></th>
							<th>Name</th>
							<th>Accession Num.</th>
							<th>Family</th>
							<th>Seq. Length</th>
							<th>MFE Acc.</th>
							<th>Native Density</th>
							<th>Predicted Density</th>
							<th>Stuffed Density</th>
							<th>Ambiguous</th>
							<th>Aligned</th>
						</tr>
						<?php populateTable(); ?>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#tabs').tabs();
		});
		$(window).load(function() {
			$('#tabs-1').append('<div id="bottomClearDiv" style="clear:both;" class="clear"></div>');
		});
	</script>
</body>
</html>