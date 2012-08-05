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
				<div id="sequenceGrid" class="leftMain">
					Coming Soon...
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