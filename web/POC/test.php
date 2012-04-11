<?php ?>
<html>
<head>

<!--<link rel="stylesheet" type="text/css" href="css/main.css" media="all" />-->
<script type="text/javascript" src="js/jquery.min.js"></script>
<!--<script type="text/javascript" src="js/jquery-1.7.1.js"></script>-->
<script type="text/javascript" src="js/jquery.utils.js"></script>
<script type="text/javascript" src="js/jquery.ui.js"></script>
<script type="text/javascript" src="js/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="js/jquery.ui.core.js"></script>
<script type="text/javascript" src="js/jquery.jcss_align.js"></script>
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
<link rel="stylesheet" href="http://jqueryui.com/themes/base/jquery.ui.all.css">

<link rel="stylesheet" type="text/css" href="css/demo.css" media="all" />
<script type="text/javascript"
	src="js/jquery.drag_drop_multi_select_alpha.js"></script>
<script type="text/javascript" src="js/demo.js"></script>
<script type="text/javascript" src="js/compbio.js"></script>

</head>
<body>
	<form enctype="multipart/form-data"
		action="scripts/public_api.php?upload=1" method="POST">
		File: <input name="uploadedfile" type="file" /><br /> <input
			type="submit" value="Upload File" />
	</form>
	<br />
	<br />
	<form enctype="multipart/form-data"
		action="scripts/public_api.php?zip=1" method="POST">
		Zip: <input name="uploadedfile" type="file" /><br /> <input
			type="submit" value="Upload File" />
	</form>
	<br />
	<br />
	<div style="border: solid 1px #000; width: 400px; padding: 10px">
	<form name="form1" action="" method="POST" enctype="multipart/form-data">
		Zip: <input id="uploaded_file" name="uploaded_file" type="file" /><br /> 
		<button class="button" id="buttonUpload" onclick="return upload_file();">Upload</button>
	</form>
	</div>
</body>
</html>
