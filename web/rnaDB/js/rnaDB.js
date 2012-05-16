
function sliderRange(ele, id, min, max) {
	if (ele && id && vals) {
		ele.html('<label for="min'+id+'">Min: </label><input type="text" id="min'+id+'" ' 
			'name="min'+id+'" style="width:60px;" value="'+min+'" />' +
			'<span id="slider'+id+'" style="width:50%;display:inline-block;margin-left:20px;margin-right:20px;"></span>'+
			'<label for="max'+id+'">Max: </label><input type="text" id="max'+id+'" ' 
			'name="max'+id+'" style="width:60px;" value="'+max+'" />');
	}
}