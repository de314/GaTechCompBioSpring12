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
	
	$("#resultsGraphs").html("");
	// Size presets
	$("#resultsGraphs").append("<button onclick='change_size(20,80);'><span>Resize Graphs: Small<span></button>");
	$("#resultsGraphs").append("<button onclick='change_size(30,120);'><span>Resize Graphs: Medium<span></button>");
	$("#resultsGraphs").append("<button onclick='change_size(40,160);'><span>Resize Graphs: Large<span></button><br />");
	// Fine size adjustment
	$("#resultsGraphs").append("<button onclick='change_size2(-5,0);'><span>Width--<span></button>");
	$("#resultsGraphs").append("<button onclick='change_size2(5,0);'><span>Width++<span></button>");
	$("#resultsGraphs").append("<button onclick='change_size2(0,-5);'><span>Height--<span></button>");
	$("#resultsGraphs").append("<button onclick='change_size2(0,5);'><span>Height++<span></button><br />");
	// Grouping
	$("#resultsGraphs").append("<p><button id='switch_bttn' onclick='switch_grouping();'><span id='switch_text'>"+ switch_button_text + "<span></button></p>");
	
	$("#resultsGraphs").append("<h3>Knudsen-Hein Probabilities</h3>");	
	format_chart(data_kh, kh_labels, true);
	
	$("#resultsGraphs").append("<h3>Nucleotide Distribution Probabilities</h3>");
	format_chart(data_nuc, nuc_labels, false);
	
	$("#resultsGraphs").append("<h3>Unpaired Nucleotide Probabilities</h3>");	
	format_chart(data_upn, nuc_labels, false);
	
	$("#resultsGraphs").append("<h3>Basepair A* Probabilities</h3>");
	format_chart(data_bp_a, bp_a_labels, true);
	
	$("#resultsGraphs").append("<h3>Basepair C* Probabilities</h3>");
	format_chart(data_bp_c, bp_c_labels, true);
	
	$("#resultsGraphs").append("<h3>Basepair G* Probabilities</h3>");
	format_chart(data_bp_g, bp_g_labels, true);
	
	$("#resultsGraphs").append("<h3>Basepair U* Probabilities</h3>");
	format_chart(data_bp_u, bp_u_labels, true);
}

function format_chart(data, labels, rotate) {
	var w = graph_width, h = graph_height;
	var x = d3.scale.linear().domain([0, 1]).range([20, 20+w]); 
	var y = d3.scale.linear().domain([0, 1]).rangeRound([0, h]);
	var yp = d3.scale.linear().domain([1, 0]).rangeRound([0, h]);
	var i = 0;
	
	var chart = d3.select("#resultsGraphs").append("svg")
	   .attr("class", "chart")
	   .attr("width", w * data.length - 1 + 2 * w)
	   .attr("height", h + 40);

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
		   .attr("width", w)
		   .attr("height", function(d) { return y(d.value); })
		   .attr("fill", function(d) { return d.color; });
	
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

function generate_matrix_plot() {

	$("#resultsGraphs").html("<h1 id='scatterplot_matrix'>Scatterplot Matrix</h1><div id='chart'> </div>");
	$("#resultsGraphs").append("<p>Scatterplot matrix design invented by J. A. Hartigan; see also <a href='http://www.r-project.org/'>R</a> and <a href='http://www.ggobi.org/'>GGobi</a>.</p>");
	
	d3.json("flowers.json", function(flower) {

	  // Size parameters.
	  var size = 150,
	      padding = 19.5,
	      n = flower.traits.length;

	  // Position scales.
	  var x = {}, y = {};
	  flower.traits.forEach(function(trait) {
	    var value = function(d) { return d[trait]; },
	        domain = [d3.min(flower.values, value), d3.max(flower.values, value)],
	        range = [padding / 2, size - padding / 2];
	    x[trait] = d3.scale.linear().domain(domain).range(range);
	    y[trait] = d3.scale.linear().domain(domain).range(range.slice().reverse());
	  });

	  // Axes.
	  var axis = d3.svg.axis()
	      .ticks(5)
	      .tickSize(size * n);

	  // Brush.
	  var brush = d3.svg.brush()
	      .on("brushstart", brushstart)
	      .on("brush", brush)
	      .on("brushend", brushend);

	  // Root panel.
	  var svg = d3.select("#chart").append("svg")
	      .attr("width", size * n + padding)
	      .attr("height", size * n + padding);

	  // X-axis.
	  svg.selectAll("g.x.axis")
	      .data(flower.traits)
	    .enter().append("g")
	      .attr("class", "x axis")
	      .attr("transform", function(d, i) { return "translate(" + i * size + ",0)"; })
	      .each(function(d) { d3.select(this).call(axis.scale(x[d]).orient("bottom")); });

	  // Y-axis.
	  svg.selectAll("g.y.axis")
	      .data(flower.traits)
	    .enter().append("g")
	      .attr("class", "y axis")
	      .attr("transform", function(d, i) { return "translate(0," + i * size + ")"; })
	      .each(function(d) { d3.select(this).call(axis.scale(y[d]).orient("right")); });

	  // Cell and plot.
	  var cell = svg.selectAll("g.cell")
	      .data(cross(flower.traits, flower.traits))
	    .enter().append("g")
	      .attr("class", "cell")
	      .attr("transform", function(d) { return "translate(" + d.i * size + "," + d.j * size + ")"; })
	      .each(plot);

	  // Titles for the diagonal.
	  cell.filter(function(d) { return d.i == d.j; }).append("text")
	      .attr("x", padding)
	      .attr("y", padding)
	      .attr("dy", ".71em")
	      .text(function(d) { return d.x; });

	  function plot(p) {
	    var cell = d3.select(this);

	    // Plot frame.
	    cell.append("rect")
	        .attr("class", "frame")
	        .attr("x", padding / 2)
	        .attr("y", padding / 2)
	        .attr("width", size - padding)
	        .attr("height", size - padding);

	    // Plot dots.
	    cell.selectAll("circle")
	        .data(flower.values)
	      .enter().append("circle")
	        .attr("class", function(d) { return d.species; })
	        .attr("cx", function(d) { return x[p.x](d[p.x]); })
	        .attr("cy", function(d) { return y[p.y](d[p.y]); })
	        .attr("r", 3);

	    // Plot brush.
	    cell.call(brush.x(x[p.x]).y(y[p.y]));
	  }

	  // Clear the previously-active brush, if any.
	  function brushstart(p) {
	    if (brush.data !== p) {
	      cell.call(brush.clear());
	      brush.x(x[p.x]).y(y[p.y]).data = p;
	    }
	  }

	  // Highlight the selected circles.
	  function brush(p) {
	    var e = brush.extent();
	    svg.selectAll("circle").attr("class", function(d) {
	      return e[0][0] <= d[p.x] && d[p.x] <= e[1][0]
	          && e[0][1] <= d[p.y] && d[p.y] <= e[1][1]
	          ? d.species : null;
	    });
	  }

	  // If the brush is empty, select all circles.
	  function brushend() {
	    if (brush.empty()) svg.selectAll("circle").attr("class", function(d) {
	      return d.species;
	    });
	  }

	  function cross(a, b) {
	    var c = [], n = a.length, m = b.length, i, j;
	    for (i = -1; ++i < n;) for (j = -1; ++j < m;) c.push({x: a[i], i: i, y: b[j], j: j});
	    return c;
	  }
	});
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

/*
function hex2rgb( colour ) {
    if ( colour[0] == '#' )
    	colour = substr( colour, 1 );
    if ( $colour.length == 6 )
    	list( $r, $g, $b ) = array( $colour[0] . $colour[1], $colour[2] . $colour[3], $colour[4] . $colour[5] );
    else if ( strlen( $colour ) == 3 )
    	list( $r, $g, $b ) = array( $colour[0] . $colour[0], $colour[1] . $colour[1], $colour[2] . $colour[2] );
    else
    	return false;
    $r = hexdec( $r );
    $g = hexdec( $g );
    $b = hexdec( $b );
    return array( 'red' => $r, 'green' => $g, 'blue' => $b );
}
*/