var kh_labels = [ "S->LS", "S->L", "L->t", "L->dFd", "F->dFd", "F->LS" ];
var nuc_labels = [ "A", "C", "G", "U"];
var bp_a_labels = [ "AA", "AC", "AG", "AU"];
var bp_c_labels = [ "CA", "CC", "CG", "CU"];
var bp_g_labels = [ "GA", "GC", "GG", "GU"];
var bp_u_labels = [ "UA", "UC", "UG", "UU"];
var group_by_value = 1;
var graph_width = 20;
var graph_height = 80;
var switch_button_text = "Group by training set";
var chartCount = 0;

function switch_grouping() {
	if (group_by_value)
		switch_button_text = "Group by value";
	else
		switch_button_text = "Group by training set";
	group_by_value = !group_by_value;
	generate_graphs();
}

function change_size(w, h) {
	graph_width = w;
	graph_height = h;
	generate_graphs();
}

function change_size2(w, h) {
	if (graph_width + w > 5)
		graph_width += w;
	if (graph_height + h > 20)
		graph_height += h;
	generate_graphs();
}

function generate_graphs() {	
	generate_bar_plot();
}

function generate_bar_plot() {

	var data_kh = new Array();
	var data_nuc = new Array();
	var data_upn = new Array();
	var data_bp_a = new Array();
	var data_bp_c = new Array();
	var data_bp_g = new Array();
	var data_bp_u = new Array();
	
	var i, j, count;
	
	if (group_by_value) {
		/* Object prob
		 * 		double[] kh		// Knudson-Hein
		 * 		double[] nuc	// Nucleotide Distribution
		 * 		double[] upn	// Unpaired Nucleotides
		 * 		double[][] bp	// Base Pairs
		 */
		// kh grammar
		for (i=0;i<kh_arr.length;i++) 
			for (var ts in trainingSets)
				data_kh.push({"time": count++, "value": trainingSets[ts].grammar.prob.kh[kh_arr[i]], "color": trainingSets[ts].color });

		// nucleotide dist
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_nuc.push({"time": count++, "value": trainingSets[ts].grammar.prob.nuc[i], "color": trainingSets[ts].color });

		// unpaired nucleotides
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_upn.push({"time": count++, "value": trainingSets[ts].grammar.prob.upn[i], "color": trainingSets[ts].color });

		// base pairs a*
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_bp_a.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[A][i], "color": trainingSets[ts].color });
		
		// base pairs c*
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_bp_c.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[C][i], "color": trainingSets[ts].color });
		
		// base pairs g*
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_bp_g.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[G][i], "color": trainingSets[ts].color });
		
		// base pairs u*
		for (i=0;i<nuc_arr.length;i++)
			for (var ts in trainingSets)
				data_bp_u.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[U][i], "color": trainingSets[ts].color });
	} else {
		for (var ts in trainingSets)
			for (i=0;i<kh_arr.length;i++) 	
				data_kh.push({"time": count++, "value": trainingSets[ts].grammar.prob.kh[kh_arr[i]], "color": trainingSets[ts].color });
		
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_nuc.push({"time": count++, "value": trainingSets[ts].grammar.prob.nuc[i], "color": trainingSets[ts].color });

		// unpaired nucleotides
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_upn.push({"time": count++, "value": trainingSets[ts].grammar.prob.upn[i], "color": trainingSets[ts].color });

		// base pairs a*
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_bp_a.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[A][i], "color": trainingSets[ts].color });
		
		// base pairs c*
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_bp_c.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[C][i], "color": trainingSets[ts].color });
		
		// base pairs g*
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_bp_g.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[G][i], "color": trainingSets[ts].color });
		
		// base pairs u*
		for (var ts in trainingSets)
			for (i=0;i<nuc_arr.length;i++)
				data_bp_u.push({"time": count++, "value": trainingSets[ts].grammar.prob.bp[U][i], "color": trainingSets[ts].color });
	}
	var jq_id = "#barGraph";
	var ele = $(jq_id);
	ele.html("");
	// Fine size adjustment
	ele.append("<button onclick='change_size2(-5,0);'><span>Width--<span></button>");
	ele.append("<button onclick='change_size2(5,0);'><span>Width++<span></button>");
	ele.append("<button onclick='change_size2(0,-5);'><span>Height--<span></button>");
	ele.append("<button onclick='change_size2(0,5);'><span>Height++<span></button>");
	// Size presets
	ele.append("<button onclick='change_size(20,80);'><span>Sm<span></button>");
	ele.append("<button onclick='change_size(30,120);'><span>Med<span></button>");
	ele.append("<button onclick='change_size(40,160);'><span>Lg<span></button><br />");
	// Grouping
	ele.append("<p><button id='switch_bttn' onclick='switch_grouping();'><span id='switch_text'>"+ switch_button_text + "<span></button></p>");
	
	ele.append("<h3>Knudsen-Hein Probabilities</h3>");	
	format_chart(data_kh, kh_labels, true, jq_id);
	
	ele.append("<h3>Nucleotide Distribution Probabilities</h3>");
	format_chart(data_nuc, nuc_labels, false, jq_id);
	
	ele.append("<h3>Unpaired Nucleotide Probabilities</h3>");	
	format_chart(data_upn, nuc_labels, false, jq_id);
	
	ele.append("<h3>Basepair A* Probabilities</h3>");
	format_chart(data_bp_a, bp_a_labels, true, jq_id);
	
	ele.append("<h3>Basepair C* Probabilities</h3>");
	format_chart(data_bp_c, bp_c_labels, true, jq_id);
	
	ele.append("<h3>Basepair G* Probabilities</h3>");
	format_chart(data_bp_g, bp_g_labels, true, jq_id);
	
	ele.append("<h3>Basepair U* Probabilities</h3>");
	format_chart(data_bp_u, bp_u_labels, true, jq_id);
}

function format_chart(data, labels, rotate, jq_id) {
	var w = graph_width, h = graph_height;
	var x = d3.scale.linear().domain([0, 1]).range([20, 20+w]); 
	var y = d3.scale.linear().domain([0, 1]).rangeRound([0, h]);
	var yp = d3.scale.linear().domain([1, 0]).rangeRound([0, h]);
	var i = 0;
	
	var chart = d3.select(jq_id).append("svg")
	   .attr("class", "chart")
	   .attr("width", w * data.length - 1 + 2 * w + 35)
	   .attr("height", h + 40)
	   .attr("id", "chart"+(chartCount++));

	// x-axis
	chart.append("line")
	   .attr("x1", w)
	   .attr("x2", w * data.length + 20)
	   .attr("y1", h + 10)
	   .attr("y2", h + 10)
	   .style("stroke", "#000");
	
	// y-axis
	chart.append("line")
	   .attr("x1", 20 - 2)
	   .attr("x2", 20 - 2)
	   .attr("y1", 10)
	   .attr("y2", h +10)
	   .style("stroke", "#000");
	
	// add y-axis
	var rules = chart.append("g");
	 rules = rules.selectAll(".rule")
	 	.data(y.ticks(4))
	 	.enter().append("g")
	 	.attr("class", "rule")
	 	.attr("transform", function(d) { return "translate(0," + (yp(d) + 10) + ")"; });
	 
	 rules.append("line")
	   .attr("x1", 15)
	   .attr("x2", w * data.length + 20)
	   .style("stroke", "#000");
	 
	 rules.append("text")
		 .attr("x", 0)
		 .attr("dy", ".35em")
		 .text(function(d) { return d; });
	
	 // plot data
	 chart.selectAll("rect")
		   .data(data)
		   .enter().append("rect")
		   .attr("x", function(d, i) { return x(i) - .5; })
		   .attr("y", function(d) { return h - y(d.value) + 8; })
		   .attr("val", function(d){return d.value;})
		   .attr("width", w)
		   .attr("height", function(d) { return y(d.value); })
		   .attr("fill", function(d) { return d.color; })
		   .attr("onmouseover", "displayY();")
		   .attr("onmouseout", "hideY();");
	
	// add labels
	var offset = 25;
	if (group_by_value) {
		var x_spacing = (data.length / labels.length) * w;
		for(i = 0;i<labels.length;i++) {
			var x1 = offset + (w/2) + i*x_spacing;
			var y1 = h+5;
			chart.append("text")
				.attr("y", y1 + 15)
				.attr("x", x1 - (rotate ? 0 : (w/2)))
				.attr("transform", rotate ? "rotate(45, "+x1+","+y1+")" : "")
				.text(labels[i]);
		}
	} else {
		for(i = 0;i<data.length;i++) {
			var x1 = offset + (w/2) + i*w;
			var y1 = h+5;
			chart.append("text")
				.attr("y", y1 + 15)
				.attr("x", x1 - (rotate ? 0 : (w/2)))
				.attr("transform", rotate ? "rotate(45, "+x1+","+y1+")" : "")
				.text(labels[i%labels.length]);
		}
	}
}

function getRGB(color) {
    var result;

    // Look for rgb(num,num,num)
    if (result = /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/.exec(color)) 
    	return [parseInt(result[1]), parseInt(result[2]), parseInt(result[3])];

    // Look for rgb(num%,num%,num%)
    if (result = /rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/.exec(color)) 
    	return [parseFloat(result[1]) * 2.55, parseFloat(result[2]) * 2.55, parseFloat(result[3]) * 2.55];

    // Look for #a0b1c2
    if (result = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/.exec(color)) 
    	return [parseInt(result[1], 16), parseInt(result[2], 16), parseInt(result[3], 16)];

    // Look for #fff
    if (result = /#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(color)) 
    	return [parseInt(result[1] + result[1], 16), parseInt(result[2] + result[2], 16), parseInt(result[3] + result[3], 16)];
}

function displayY() {
	var e = window.event;
	if (e) {
		updateY(e);
		$(e.target).mousemove(updateY);
	}
}

function updateY(e) {
	if (e) {
		var ele = $('#posText');
		if (ele)
			ele.remove();
		var jq_id = "#"+e.target.parentElement.id;
		ele = $(jq_id);
		var pos = {};
		pos.x = e.pageX - ele.position().left;
		pos.y = e.pageY - ele.position().top;
		d3.select(jq_id).append('text')
			.attr('id', 'posText')
			.attr('x', pos.x)
			.attr('y', pos.y - 20)
			.text($(e.target).attr("val"));
		
	}
}

function hideY() {
	var ele = $('#posText');
	if (ele)
		ele.remove();
	$(document).mousemove();
}
