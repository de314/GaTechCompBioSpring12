<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Percent Identity: F-Measure Comparison</title>
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="../jquery.js"></script>
    <script language="javascript" type="text/javascript" src="../jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="../jquery.flot.navigate.js"></script>
    <script language="javascript" type="text/javascript" src="16S_FmComp.js?ts=<?php echo time();?>"></script>
    <script language="javascript" type="text/javascript" src="../REU_flot_lib.js?ts=<?php echo time();?>"></script>
    <style type="text/css">
    #placeholder .button {
        position: absolute;
        cursor: pointer;
    }
    #placeholder div.button {
        font-size: smaller;
        color: #999;
        background-color: #eee;
        padding: 2px;
    }
    .message {
        padding-left: 50px;
        font-size: smaller;
    }
    .s {
        background-color: rgb(0, 0, 0);
    }
    .d {
        background-color: rgb(255, 255, 255);
    }
    </style>
 </head>
 <body>
    <h1>Percent Identity: F-Measure Comparison</h1>
    <p style="border: 1px solid #000;padding:10px;"></p>
    <div style="padding-bottom:30px;">
        <form onsubmit="myFlotResize(); return false;"> 
	Width=<input type="text" id="graphWidth">&emsp;Height=<input type="text" id="graphHeight"><input type="submit" value="Resize">
	<input type="button" value="Zoom" onclick="handleZoom();" /> Zoom Automatically <input type="checkbox" id="autoZoom" checked="checked" />
        </form>
    </div>
    <div id="placeholder" style="width:450px;height:450px;float:left;"></div>
    <div id="legendHolder" style="float:left;"></div>
    <div id="selectionTable" style="float:left;">
	
    </div>
 </body>
</html>



