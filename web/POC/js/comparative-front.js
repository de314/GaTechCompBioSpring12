var smgList = [];


$(document).ready(function() {
	getSmallGraph(0);
	getSmallGraph(1);
	getSmallGraph(2);
	updateCompFront();
});

function updateCompFront() {
	for (g in smgList)
		smgList[g].update($('#Xax').val(), $('#Yax').val());
}

function getSmallGraph(id) {
	var g = {};
	g.id = id;
	g.Xax = 0;
	g.Yax = 0;
	g.locked = false;
	g.lockEle = $('#smgLock' + id);
	g.title = "";
	g.titleEle = $('#smgTitle' + id);
	g.svg = $('#smgSVG' + id);
	g.chart = $('#chartSM' + id);
	g.update = function(x, y) {
		if (!this.locked) {
			this.Xax = $('#Xax').get(0).selectedIndex;
			this.Yax = $('#Yax').get(0).selectedIndex;
			this.title = 'X: ' + x + '<br />Y: ' + y;
			this.titleEle.html(this.title);
			// auto zoom
			newTightPlot(this.chart, false);
			// full range
			//newPlot2(selXdata, selYdata, { 'minY':0, 'maxY':1, 'minX':0, 'maxX':1 }, this.chart, false);
		}
	};
	g.tog = function() {
		this.locked = !this.locked;
		if (this.locked)
			this.lockEle.removeClass('ui-icon-unlocked').addClass('ui-icon-locked');
		else
			this.lockEle.removeClass('ui-icon-locked').addClass('ui-icon-unlocked');
	};
	g.maximize = function() {
		if (this.locked) {
			$('#Xax').get(0).selectedIndex = this.Xax;
			$('#Yax').get(0).selectedIndex = this.Yax;
			newPlot();
		}
	};
	smgList.push(g);
	return g;
}
