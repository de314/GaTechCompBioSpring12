<?php
session_start();

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
	<link type="text/css" href="css/main.css" rel="stylesheet" />
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript">
	var max = <?php echo getSize_db($_POST, 100000); ?>;
	var searchParams = <?php echo json_encode($_POST); ?>;
	</script>
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