(function($) {
	$.debug = function(message) {if(window.console) {console.log(message);} else {alert(message);}};
	$.fn.exists = function(){if(jQuery(this).length == 0){return false;}else{return true;}};
	$.browser_size = function() {
		if ($.browser.msie) {
			var document_width = $(window).height();
			var document_height = $(document).height();
			return [
				window.innerWidth || 						// ie7+
				document.documentElement.clientWidth || 	// ie6  
				document.body.clientWidth, 					// ie6 quirks mode
				document_height - document_width < 20 ? document_width : document_height
			];
		}
		return [$(window).width(), $(window).height()];
	};
	$.fn.images_loader = function() {
		return this.each(function() {
			var self = this;
			var $self = $(this);
			var number_of_images_preloaded = 0;
			var total_img_to_loaded;
			var images_loaded = false;
			
			$.extend(self, {
				image_load: function(number_of_images_preloaded,total_img_to_load,images_loaded) {
					number_of_images_preloaded++;
					if(number_of_images_preloaded >= total_img_to_load && images_loaded != true){
						images_loaded = true;
						$.fn.images_load.afterLoad($self);
						return true;
					}
					return self;
				}
			});
			
			
			if(self.tagName == "IMG") {
				total_img_to_loaded = 1;
				var image = new Image();
				image.src = $self.attr('src');
				if(image.complete){
					self.image_load(number_of_images_preloaded,total_img_to_loaded,images_loaded);
				}
				else{
					image.onload = self.image_load(number_of_images_preloaded,total_img_to_loaded,images_loaded);
				}
			}
			else if($self.find('img').exists()) {
				$self.find('img').each(function() {
					var image = new Image();
					image.src = $(this).attr('src');
					if (image.complete)        
					self.image_load();        
					else        
					image.onload = self.image_load;
				});
			}
			else {
			}
		});
	};
	$.fn.images_loader.afterLoad = function($obj){
		$obj.css({width:100});
	}
})(jQuery);