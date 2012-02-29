var selectedList = 0;

$(document).ready(function(){
    // Set the default function
    $.fn.drag_drop_multi_select.defaults.after_drop_action = function($item,$old,$new,e,ui){
        // Possible param $item_instance, $old_container, $new_container, event, helper
        var $target = $(e.target);
        $target.find('ul').append($item);
    };
    // Initiate the drag & drop
    $('#drag_drop').drag_drop_multi_select({
        element_to_drag_drop_select:'.list ul li',
        elements_to_drop:'.list',
        elements_to_cancel_select:'.title'
    });
    // Align the selection bar
    $('#drag_drop .items_selected').JCSS_align({
        vertical_align:'bottom',
        horizontal_align:'left'
    });
    $(function() {
        $( "#tabs" ).tabs();
    });
});

function resetLists(){
    // Set the default function
    $.fn.drag_drop_multi_select.defaults.after_drop_action = function($item,$old,$new,e,ui){
        // Possible param $item_instance, $old_container, $new_container, event, helper
        var $target = $(e.target);
        $target.find('ul').append($item);
    };
    // Initiate the drag & drop
    $('#drag_drop').drag_drop_multi_select({
        element_to_drag_drop_select:'.list ul li',
        elements_to_drop:'.list',
        elements_to_cancel_select:'.title'
    });
    // Align the selection bar
    $('#drag_drop .items_selected').JCSS_align({
        vertical_align:'bottom',
        horizontal_align:'left'
    });
}

function addTrainingSet(title) {
	$('.container').append("<div class='list'><div class='title' onclick='selTitle(this)'>"+title+"&nbsp;<span style='color:red' onclick='deleteTSet(this)'>X</span></div><ul></ul></div></div>");
	// TODO: Handle sequences
	var arr = $('#seqs').val().split(",");
	var i;
      for (i=0;i<arr.length;i++)
		$('ul:last').append("<li>"+arr[i]+"</li>");
	resetLists();
}

function deleteTSet(ele) {
	$(ele.parentNode.parentNode).remove();
}

function selTitle(ele) {
      if (selectedList)
		selectedList.css("border","");
      selectedList = $(ele.parentNode);
	selectedList.css("border","2px solid #000");
}

function setColor(ele) {
	if (selectedList)
		selectedList.css("background-color", $(ele).css("background-color"));
}
