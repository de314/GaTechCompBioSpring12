<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Pfold Grammar Probabilities Interactive Graph</title>
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="../jquery.js"></script>
    <script language="javascript" type="text/javascript" src="../jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="../jquery.flot.navigate.js"></script>
    <script language="javascript" type="text/javascript" src="5S_GrammarParams.js?ts=<?php echo time();?>"></script>
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
    <h1>Pfold Grammar Probabilities Interactive Graph</h1>
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
	<table border="1"><tr><th>Name</th><th>x-axis</th><th>y-axis</th></tr><tr><td>Con_BPs</td><td class="d"onclick="selTDX($(this), Con_BPs);">&emsp;</td><td class="d" onclick="selTDY($(this), Con_BPs);">&emsp;</td></tr><tr><td>F_LS</td><td class="d"onclick="selTDX($(this), F_LS);">&emsp;</td><td class="d" onclick="selTDY($(this), F_LS);">&emsp;</td></tr><tr><td>F_dFd</td><td class="d"onclick="selTDX($(this), F_dFd);">&emsp;</td><td class="d" onclick="selTDY($(this), F_dFd);">&emsp;</td></tr><tr><td>L_dFd</td><td class="d"onclick="selTDX($(this), L_dFd);">&emsp;</td><td class="d" onclick="selTDY($(this), L_dFd);">&emsp;</td></tr><tr><td>L_s</td><td class="d"onclick="selTDX($(this), L_s);">&emsp;</td><td class="d" onclick="selTDY($(this), L_s);">&emsp;</td></tr><tr><td>NonCon_BPs</td><td class="d"onclick="selTDX($(this), NonCon_BPs);">&emsp;</td><td class="d" onclick="selTDY($(this), NonCon_BPs);">&emsp;</td></tr><tr><td>S_L</td><td class="d"onclick="selTDX($(this), S_L);">&emsp;</td><td class="d" onclick="selTDY($(this), S_L);">&emsp;</td></tr><tr><td>S_LS</td><td class="d"onclick="selTDX($(this), S_LS);">&emsp;</td><td class="d" onclick="selTDY($(this), S_LS);">&emsp;</td></tr><tr><td>WC_BPs</td><td class="d"onclick="selTDX($(this), WC_BPs);">&emsp;</td><td class="d" onclick="selTDY($(this), WC_BPs);">&emsp;</td></tr><tr><td>Wb_BPs</td><td class="d"onclick="selTDX($(this), Wb_BPs);">&emsp;</td><td class="d" onclick="selTDY($(this), Wb_BPs);">&emsp;</td></tr><tr><td>a</td><td class="d"onclick="selTDX($(this), a);">&emsp;</td><td class="d" onclick="selTDY($(this), a);">&emsp;</td></tr><tr><td>aa</td><td class="d"onclick="selTDX($(this), aa);">&emsp;</td><td class="d" onclick="selTDY($(this), aa);">&emsp;</td></tr><tr><td>ac</td><td class="d"onclick="selTDX($(this), ac);">&emsp;</td><td class="d" onclick="selTDY($(this), ac);">&emsp;</td></tr><tr><td>ag</td><td class="d"onclick="selTDX($(this), ag);">&emsp;</td><td class="d" onclick="selTDY($(this), ag);">&emsp;</td></tr><tr><td>au</td><td class="d"onclick="selTDX($(this), au);">&emsp;</td><td class="d" onclick="selTDY($(this), au);">&emsp;</td></tr><tr><td>c</td><td class="d"onclick="selTDX($(this), c);">&emsp;</td><td class="d" onclick="selTDY($(this), c);">&emsp;</td></tr><tr><td>ca</td><td class="d"onclick="selTDX($(this), ca);">&emsp;</td><td class="d" onclick="selTDY($(this), ca);">&emsp;</td></tr><tr><td>cc</td><td class="d"onclick="selTDX($(this), cc);">&emsp;</td><td class="d" onclick="selTDY($(this), cc);">&emsp;</td></tr><tr><td>cg</td><td class="d"onclick="selTDX($(this), cg);">&emsp;</td><td class="d" onclick="selTDY($(this), cg);">&emsp;</td></tr><tr><td>cu</td><td class="d"onclick="selTDX($(this), cu);">&emsp;</td><td class="d" onclick="selTDY($(this), cu);">&emsp;</td></tr><tr><td>g</td><td class="d"onclick="selTDX($(this), g);">&emsp;</td><td class="d" onclick="selTDY($(this), g);">&emsp;</td></tr><tr><td>ga</td><td class="d"onclick="selTDX($(this), ga);">&emsp;</td><td class="d" onclick="selTDY($(this), ga);">&emsp;</td></tr><tr><td>gc</td><td class="d"onclick="selTDX($(this), gc);">&emsp;</td><td class="d" onclick="selTDY($(this), gc);">&emsp;</td></tr><tr><td>gg</td><td class="d"onclick="selTDX($(this), gg);">&emsp;</td><td class="d" onclick="selTDY($(this), gg);">&emsp;</td></tr><tr><td>gu</td><td class="d"onclick="selTDX($(this), gu);">&emsp;</td><td class="d" onclick="selTDY($(this), gu);">&emsp;</td></tr><tr><td>u</td><td class="d"onclick="selTDX($(this), u);">&emsp;</td><td class="d" onclick="selTDY($(this), u);">&emsp;</td></tr><tr><td>ua</td><td class="d"onclick="selTDX($(this), ua);">&emsp;</td><td class="d" onclick="selTDY($(this), ua);">&emsp;</td></tr><tr><td>uc</td><td class="d"onclick="selTDX($(this), uc);">&emsp;</td><td class="d" onclick="selTDY($(this), uc);">&emsp;</td></tr><tr><td>ug</td><td class="d"onclick="selTDX($(this), ug);">&emsp;</td><td class="d" onclick="selTDY($(this), ug);">&emsp;</td></tr><tr><td>uu</td><td class="d"onclick="selTDX($(this), uu);">&emsp;</td><td class="d" onclick="selTDY($(this), uu);">&emsp;</td></tr></table>
    </div>
 </body>
</html>



