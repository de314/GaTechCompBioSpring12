<?php
session_start();
// TODO: Check that user is logged in through CAS else redirect

include_once 'scripts/rnadb_api.php';

if (!isset($_POST['family']))
	header("Location: index.php");

/*
Array
(
    [family] => tRna,5S,16S,23S
    [ambiguous] => true
    [aligned] => true
    [lenmin] => 0
    [lenmax] => 3000
    [mfeaccmin] => 0
    [mfeaccmax] => 1000
    [name] =>  
    [natdenmin] => 0
    [natdenmax] => 1000
    [preddenmin] => 0
    [preddenmax] => 1000
    [stuffeddenmin] => 0
    [stuffeddenmax] => 1000
)
 */

function getRnaHtml($rna) {
	return '<tr><td><input type="checkbox" rnaId='.$rna['rid'].' /></td><td>
		'.$rna['name'].'</td>
		<td>'.$rna["family"].'</td>
		<td>'.$rna["seqLength"].'</td>
		<td>'.$rna["mfeAcc"].'</td>
		<td>'.$rna["natDensity"].'</td>
		<td>'.$rna["predDensity"].'</td>
		<td>'.$rna["stuffedDensity"].'</td>
		<td>'.($rna["ambiguous"]?"Yes":"No").'</td>
		<td><a href="#" onclick="alert(\''.$rna["alignment"].'\');">View</a></td></tr>';
}

function populateTable($searchParams) {
	$arr = getSequences($searchParams);
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
		function downloadOut(all) {
			var selected = "";
			$("INPUT[type='checkbox']").each(function() {
				var ele = $(this);
				if ((all | ele.attr('checked')=='checked') && ele.attr('rnaId')) {
					selected += ele.attr('rnaId') + ",";
				}
			});
			console.debug(selected);
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
			$("#downloadLinks").html("<img src='images/loading_small.gif' />");
		}
		function downloadIn(data) {
			console.debug(data);
			$("#downloadLinks").html("");
			var obj = eval('(' + data + ')');
			if (obj.link) {
				$("<span'>Download Zipped File: <a href='" + obj.link + "'>" + obj.link + "</a></span>")
					.hide()
					.appendTo("#downloadLinks")
					.fadeIn(2000);
			} else {
				$("<span style='color:red;'>Download Error: " + obj.error + "</span>")
					.hide()
					.appendTo("#downloadLinks")
					.fadeIn(2000);
			}
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
					<a href="index.php"><div class="divLink topLink">Home</div></a>
					<a href="search.php"><div class="divLink">Search</div></a>
					<a href="help.php"><div class="divLink">Help</div></a>
					<a href="help.php?moreInfo"><div class="divLink botLink">More Information</div></a>
				</div>
				<!-- Content-->
				<div id="downloadButtons" class="leftMain">
					<button onclick="downloadOut(true);">Download All Sequences</button>
					<button onclick="downloadOut(false);">Download Selected Sequences</button>
				</div>
				<div id="downloadLinks" class="leftMain">
					<i>Download links will appear here.</i>
				</div>
				<div id="sequenceGrid" class="leftMain">
					<table border="1px"; id="rnaTable">
						<tr>
							<th><input id="cbAll" type="checkbox" onclick="changeAllCheckboxes();" /></th>
							<th>Name</th>
							<th>Family</th>
							<th>Seq. Length</th>
							<th>MFE Acc.</th>
							<th>Native Density</th>
							<th>Predicted Density</th>
							<th>Stuffed Density</th>
							<th>Ambiguous</th>
							<th>Aligned</th>
						</tr>
						<?php populateTable($_POST); ?>
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