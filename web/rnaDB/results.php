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
		<td>'.($rna["alignment"]?"Yes":"No").'</td>';
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
		var searchParams = <?php echo json_encode($_POST); ?>;
		var offset = 0;
		var setSize = 100;
		var max = <?php echo getSize_db($_POST, 100000); ?>;
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
		.rnaTable {
		 	font-size: small;
		}
		.downloadButton {
		 	font-size: small;
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
					<button onclick="downloadOut('all');" class="downloadButton">Download All Sequences</button>
					<button onclick="downloadOut('curr');" class="downloadButton">Download Sequences On Current Page</button>
					<button onclick="downloadOut('selected');" class="downloadButton">Download Selected Sequences</button>
				</div>
				<div id="downloadLinks" class="leftMain">
					<i>Download links will appear here.</i>
				</div>
				<div id="downloadLinksLoading" class="leftMain">
					&nbsp;
				</div>
				<div id="sequenceGrid" class="leftMain">
					<div id="setSelectionLinks1"></div>
					<table border="1px"; class="rnaTable" id="rnaTable">
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
						<span id="resultsBody">
						<?php populateTable($_POST); ?>
						</span>
					</table>
					<div id="setSelectionLinks2"></div>
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
			populateSetSelectionLinks();
		});
	</script>
</body>
</html>