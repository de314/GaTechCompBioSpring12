$(document).ready(
		function() {
			// Set the default function
			$.fn.drag_drop_multi_select.defaults.after_drop_action = function(
					$item, $old, $new, e, ui) {
				// Possible param $item_instance, $old_container,
				// $new_container, event, helper
				var $target = $(e.target);
				$target.find('ul').append($item);
			};
			// Initiate the drag & drop
			$('#drag_drop').drag_drop_multi_select({
				element_to_drag_drop_select : '.list ul li',
				elements_to_drop : '.list',
				elements_to_cancel_select : '.title'
			});
			// Align the selection bar
			$('#drag_drop .items_selected').JCSS_align({
				vertical_align : 'bottom',
				horizontal_align : 'left'
			});
			$(function() {
				$("#tabs").tabs();
			});
		});

function resetLists() {
	// Set the default function
	$.fn.drag_drop_multi_select.defaults.after_drop_action = function($item,
			$old, $new, e, ui) {
		// Possible param $item_instance, $old_container, $new_container, event,
		// helper
		var $target = $(e.target);
		$target.find('ul').append($item);
		
		// MY CODE //
		updateTrainingSets();
	};
	// Initiate the drag & drop
	$('#drag_drop').drag_drop_multi_select({
		element_to_drag_drop_select : '.list ul li',
		elements_to_drop : '.list',
		elements_to_cancel_select : '.title'
	});
	// Align the selection bar
	$('#drag_drop .items_selected').JCSS_align({
		vertical_align : 'bottom',
		horizontal_align : 'left'
	});
	
	updateTrainingSets();
}

function updateTrainingSets() {
	for ( var i in trainingSets) {
		var ts = trainingSets[i];
		ts.clear();
		$(ts.ele).find("ul li").each(
				function(index) {
					if ($(this).attr("ddms_drag") == null)
						ts.add(new Sequence($(this), $(this).text(), $(
								this).attr("seq"), $(this).attr("str")));
				});
	}
	$("#resultsText").html("Train the grammars to see results here");
	$("#resultsGraphs").html("Train the grammars to see results here");
}
