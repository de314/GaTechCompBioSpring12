var plot = 0;
var chartLG = 0;
var chartSM0 = 0;
var crossRef = 0;
var selXdata = 0;
var selYdata = 0;
var predefinedData = 0;
var data = 0;
var keys = [];


$(document).ready(function () {
	chartLG = $("#chartLG");
    $("#graphWidth").val(chartLG.css("width"));
    $("#graphHeight").val(chartLG.css("height"));

    chartSM0 = $("#chartSM0");
    chartSM1 = $("#chartSM1");
    chartSM2 = $("#chartSM2");
    
    selX = $("#Xax");
    selY = $("#Yax");

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
    chartLG.bind("plothover", function (event, pos, item) {
        $("#x").text(pos.x.toFixed(2));
        $("#y").text(pos.y.toFixed(2));
	    if (item) {
		if (previousPoint != item.seriesIndex) {
		    previousPoint = item.seriesIndex;
		    $("#tooltip").remove();
		    var x = item.datapoint[0].toFixed(2),
		        y = item.datapoint[1].toFixed(2);
		    showTooltip(item.pageX, item.pageY,
		                keys[item.seriesIndex] + ":<br />(" + x + ", " + y + ")");
		}
	    }
	    else {
		$("#tooltip").remove();
		previousPoint = null;            
	    }
    });
});

function setVariables() {
	data = { S_L:[], S_LS:[], L_t:[], L_dFd:[], F_dFd:[], F_LS:[],
			da:[], dc:[], dg:[], du:[], una:[], unc:[], ung:[], unu:[],
			aa:[], ac:[], ag:[], au:[], ca:[], cc:[], cg:[], cu:[], 
			ga:[], gc:[], gg:[], gu:[], ua:[], uc:[], ug:[], uu:[],
			color:[], name:[], setSize:0 };
	if (trainingSets) {
		for (var ts in trainingSets) {
			var g = trainingSets[ts].grammar;
			if (g) {
				data.S_LS.push(g.prob.kh[S_LS]);
				data.S_L.push(g.prob.kh[S_L]);
				data.L_t.push(g.prob.kh[L_t]);
				data.L_dFd.push(g.prob.kh[L_dFd]);
				data.F_dFd.push(g.prob.kh[F_dFd]);
				data.F_LS.push(g.prob.kh[F_LS]);
				data.da.push(g.prob.nuc[A]);
				data.dc.push(g.prob.nuc[C]);
				data.dg.push(g.prob.nuc[G]);
				data.du.push(g.prob.nuc[U]);
				data.una.push(g.prob.upn[A]);
				data.unc.push(g.prob.upn[C]);
				data.ung.push(g.prob.upn[G]);
				data.unu.push(g.prob.upn[U]);
				data.aa.push(g.prob.bp[A][A]);
				data.ac.push(g.prob.bp[A][C]);
				data.ag.push(g.prob.bp[A][G]);
				data.au.push(g.prob.bp[A][U]);
				data.ca.push(g.prob.bp[C][A]);
				data.cc.push(g.prob.bp[C][C]);
				data.cg.push(g.prob.bp[C][G]);
				data.cu.push(g.prob.bp[C][U]);
				data.ga.push(g.prob.bp[G][A]);
				data.gc.push(g.prob.bp[G][C]);
				data.gg.push(g.prob.bp[G][G]);
				data.gu.push(g.prob.bp[G][U]);
				data.ua.push(g.prob.bp[U][A]);
				data.uc.push(g.prob.bp[U][C]);
				data.ug.push(g.prob.bp[U][G]);
				data.uu.push(g.prob.bp[U][U]);
				data.color.push(trainingSets[ts].color);
				data.name.push(trainingSets[ts].name);
				data.setSize++;
			} else {
				// error
			}
		}
		if (data.setSize)
			newPlot();
	}
}

function newPlot() {
	selXdata = data[$('#Xax').val()];
	selYdata = data[$('#Yax').val()];
	// full range
	//newPlot2(selXdata, selYdata, { 'minY':0, 'maxY':1, 'minX':0, 'maxX':1 });
	// auto zoom
	newTightPlot(chartLG, true);
	updateCompFront();
}

function newPlot2(data1, data2, newWindow, chart, large) {
	if (!data1 || !data2 || !data1.length || !data2.length)
		return; // TODO: no data... alert?
	var set = [];
	keys = [];
	for(var i=0;i<data.setSize;i++) {
	    set.push({ label: data.name[i], color: data.color[i], data : [[data1[i], data2[i]]] });
	    keys.push(data.name[i]);
	}
    newPlot3(set, newWindow, chart, large);
}

function newPlot3(set, newWindow, chart, large) {
	var options;
	if (large) {
		options = {
			series: { points: { show: true }, shadowSize: 0 },
	        yaxis: { label: $('#Yax').text(), zoomRange: [0.001, 1.25], panRange: [0, 1], min: newWindow.minY, max: newWindow.maxY },
	        xaxis: { label: $('#Xax').text(), zoomRange: [0.001, 1.25], panRange: [0, 1], min: newWindow.minX, max: newWindow.maxX },
	        zoom: { interactive: true },
	        pan: { interactive: true },
	        legend: { show: true, container: $("#legendHolder") },
	        grid: { hoverable: true }
		};
	} else {
		options = {
				series: { points: { show: true }, shadowSize: 0 },
		        yaxis: { min: newWindow.minY, max: newWindow.maxY },
		        xaxis: { min: newWindow.minX, max: newWindow.maxX },
		        zoom: { interactive: false },
		        pan: { interactive: false },
		        legend: { show: false },
		        grid: { hoverable: true }
			};
	}
    plot = $.plot(chart, set, options);
}

function combineData(combined, values1, values2) {
	for (var i=0;i<values1.length;i++)
		combined.push([ values1[i], values2[i] ]);
	return combined;
}

function newTightPlot(chart, large) {
	var i;
	var newWindow = { 'minX':1, 'minY':1, 'maxX':0, 'maxY':0 };
	if (selYdata && selXdata) {
		getMinMax(selYdata, 0, newWindow);
		getMinMax(selXdata, 1, newWindow);
	} else {
		return;
	}
	var diffX = newWindow.maxX-newWindow.minX;
	var diffY = newWindow.maxY-newWindow.minY;
	if (diffX > diffY)
		newWindow.maxY = newWindow.minY + diffX;
	else
		newWindow.maxX = newWindow.minX + diffY;
	newPlot2(selXdata, selYdata, newWindow,chart, large);
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