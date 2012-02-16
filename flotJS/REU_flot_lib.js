var plot = 0;
var placeholder = 0;
var crossRef = 0;
var selX = 0;
var selXdata = 0;
var selY = 0;
var selYdata = 0;
var predefinedData = 0;

$(document).ready(function () {
    placeholder = $("#placeholder");
    $("#graphWidth").val(placeholder.css("width"));
    $("#graphHeight").val(placeholder.css("height"));

    selX = $("#selX");
    selY = $("#selY");
    
    var newWindow = { 'minY':0, 'maxY':1, 'minX':0, 'maxX':1 };

    if (!predefinedData)
        newPlot(selXdata, selYdata, newWindow);
    else
        newPlot3(predefinedData, newWindow);

    function showTooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css( {
            position: 'absolute',
            display: 'none',
            top: y + 5,
            left: x + 5,
            border: '1px solid #fdd',
            padding: '2px',
            'background-color': '#fee',
            opacity: 0.80
        }).appendTo("body").fadeIn(200);
    }

    var previousPoint = null;
    $("#placeholder").bind("plothover", function (event, pos, item) {
        $("#x").text(pos.x.toFixed(2));
        $("#y").text(pos.y.toFixed(2));
	    if (item) {
		if (previousPoint != item.dataIndex) {
		    previousPoint = item.dataIndex;
		    
		    $("#tooltip").remove();
		    var x = item.datapoint[0].toFixed(2),
		        y = item.datapoint[1].toFixed(2);
		    showTooltip(item.pageX, item.pageY,
		                keys.keys[item.dataIndex] + ":<br />(" + x + ", " + y + ")");
		}
	    }
	    else {
		$("#tooltip").remove();
		previousPoint = null;            
	    }
    });
});

function newPlot(data1, data2, newWindow) {
	if (data1 && data1.easy) {
		var easyData = combineData([], data1.easy, data2.easy);
		var mediumData = combineData([], data1.medium, data2.medium);
		var hardData = combineData([], data1.hard, data2.hard);
		newPlot2(easyData, mediumData, hardData, newWindow);
	} else {
		newPlot2(easy, medium, hard, newWindow);
	}
}

function newPlot2(easyData, mediumData, hardData, newWindow) {
    var data = [ { label: "easy", color: "rgb(0, 255, 0)", data : easyData }, 
                 { label: "medium", color: "rgb(0, 0, 255)", data : mediumData }, 
                 { label: "hard", color: "rgb(255, 0, 0)", data : hardData } ];
    newPlot3(data, newWindow);
}

function newPlot3(data, newWindow) {
     if (predefinedData)
        data = predefinedData;
     var options = {
        series: { points: { show: true }, shadowSize: 0 },
        yaxis: { label: "F-Measure", zoomRange: [0.001, 1.25], panRange: [0, 1], min: newWindow.minY, max: newWindow.maxY },
        xaxis: { label: "F-Measure", zoomRange: [0.001, 1.25], panRange: [0, 1], min: newWindow.minX, max: newWindow.maxX },
        zoom: { interactive: true },
        pan: { interactive: true },
        legend: { show: true, container: $("#legendHolder") },
        grid: { hoverable: true }
    };
    plot = $.plot(placeholder, data, options);
}

function combineData(combined, values1, values2) {
	for (var i=0;i<values1.length;i++)
		combined.push([ values1[i], values2[i] ]);
	return combined;
}

function myFlotResize() {
	placeholder.css({ width: $("#graphWidth").val(), height: $("#graphHeight").val() });
	plot.resize();
	plot.setupGrid();
	plot.draw();
	return false;
}

function selTDX(currTD, data) {
	selX.removeClass("s").addClass("d");
	selX = currTD;
	selXdata = data;
	selX.removeClass("d").addClass("s");
	var newWindow = { 'minY':0, 'maxY':1, 'minX':0, 'maxX':1 };
	newPlot(selXdata, selYdata, newWindow);
	if ($("#autoZoom").attr('checked'))
		handleZoom();
}

function selTDY(currTD, data) {
	selY.removeClass("s").addClass("d");
	selY = currTD;
	selYdata = data;
	selY.removeClass("d").addClass("s");
	var newWindow = { 'minY':0, 'maxY':1, 'minX':0, 'maxX':1 };
	newPlot(selXdata, selYdata, newWindow);
	if ($("#autoZoom").attr('checked'))
		handleZoom();
}

function handleZoom() {
	var i;
	var newWindow = { 'minX':1, 'minY':1, 'maxX':0, 'maxY':0 };
	if (selYdata.easy) {
		getMinMax(selYdata.easy, 0, newWindow);
		getMinMax(selXdata.easy, 1, newWindow);
		getMinMax(selYdata.medium, 0, newWindow);
		getMinMax(selXdata.medium, 1, newWindow);
		getMinMax(selYdata.hard, 0, newWindow);
		getMinMax(selXdata.hard, 1, newWindow);
	} else {
		getMinMax(selYdata, newWindow);
		getMinMax(selXdata, newWindow);
	}
	var diffX = newWindow.maxX-newWindow.minX;
	var diffY = newWindow.maxY-newWindow.minY;
	if (diffX > diffY)
		newWindow.maxY = newWindow.minY + diffX;
	else
		newWindow.maxX = newWindow.minX + diffY;
	newPlot(selXdata, selYdata, newWindow);
}

function getMinMax(data, xData, newWindow) {
	var i;
	var minVal = 1;
	var maxVal = 0;	
	for (i=0;i<data.length;i++) {
		if (xData) {
			newWindow.minX = newWindow.minX < data[i] ? newWindow.minX : data[i];
			newWindow.maxX = newWindow.maxX > data[i] ? newWindow.maxX : data[i];
		} else {
			newWindow.minY = newWindow.minY < data[i] ? newWindow.minY : data[i];
			newWindow.maxY = newWindow.maxY > data[i] ? newWindow.maxY : data[i];
		}
	}
}
