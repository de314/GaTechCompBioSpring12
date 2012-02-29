/**
 * Align Everything everywhere
 * Work on all broswer platform include IE6
 * 
 * Copyright (c) 2010 Samuel Cléroux-Bouthillier
 * http://www.quatrecentquatre.com
 *
 * Dual licensed under MIT and GNU General Public License version 3 (GPLv3)
 * http://www.opensource.org/licenses
 * Launch  : April 2010
 */
 var instance_array = new Array();
 var config_array= new Array();
 var objects_array = new Array(instance_array,config_array);
 var config;
(function($) {
	$.fn.exists = function(){if(jQuery(this).length == 0){return false;}else{return true;}}
	$.fn.JCSS_align = function(settings){
		var defaults = {  
			axis:"xy",
			vertical_align:"middle",// top,middle,bottom
			vertical_offset:0,
			horizontal_align:"middle",// left,middle,right
			horizontal_offset:0,
			index:1
		};
		var config = $.extend(defaults, settings);
		return this.each(function() {
			instance_array.push(this);
			config_array.push(config);		  
			$.jcss_align_creater(this,config);
		});
	}
	$.jcss_align_creater = function(object,config){
		return object.JCSS_align || (object.JCSS_align=new $.jcss_align(object,config));
	}
	$.jcss_align = function(object,config) {
		var self = object;
		var $self = $(object);
		var parent = $(object).parent();
		var parentNodeName = parent[0].nodeName;
		var error = false;
		var horizontal_align_error = false;
		var vertical_align_error = false;
		var axis_error = false;
		var total_img_to_load = $self.find('img').size();
		var number_of_images_preloaded = 0;
		var images_loaded = false;
		
		
		if(typeof(config.vertical_offset) != "number") {
			config.vertical_offset = 0;
		}
		if(typeof(config.horizontal_offset) != "number") {
			config.horizontal_offset = 0;
		}
		$.extend(self, {
			init: function() {
				if($self.css("position") != "absolute")
				$self.css({position:"absolute"});
				
				if($self.css("z-index") == 'auto')
				$self.css({"z-index":config.index});
				
				if(parentNodeName == "BODY") {
					var sizeBrowser = viewport();
					var parentWidth = sizeBrowser[0];
					var parentHeight = sizeBrowser[1];
					var leftValue,topValue;
					if(config.horizontal_align == "left") {
						leftValue = 0;
						leftValue += config.horizontal_offset;
					}
					else if(config.horizontal_align == "middle") {
						leftValue = parentWidth/2;
						leftValue -= $self.outerWidth()/2;
					}
					else if(config.horizontal_align == "right") {
						leftValue = parentWidth;
						leftValue -= $self.outerWidth();
						leftValue -= config.horizontal_offset;
					}
					else {
						horizontal_align_error = true;
						horizontal_align_error_message = "horizontal_align : choose a valid options.\n";
						error = true;
						leftValue =0;
					}
					 
					if(config.vertical_align == "top") {
						topValue = 0;
						topValue += config.vertical_offset;
					}
					else if(config.vertical_align == "middle") {
						topValue = parentHeight/2;
						topValue -= $self.outerHeight()/2;
					}
					else if(config.vertical_align == "bottom") {	
						topValue = parentHeight;
						topValue -= $self.outerHeight();
						topValue -= config.vertical_offset;
					}
					else {
						vertical_align_error = true;
						vertical_align_error_message = "vertical_align : choose a valid options.\n";
						error = true;
						topValue =0;
					}
					
				}
				else {
					if(parent.css("position") != "relative" && parent.css("position") != "absolute")
					parent.css({position:"relative"});
					
					parentWidth = parent.outerWidth();
					parentHeight = parent.outerHeight();
					
					if(config.horizontal_align == "left") {
						leftValue = 0;
						leftValue += config.horizontal_offset;
					}
					else if(config.horizontal_align == "middle") {
						leftValue = parentWidth/2;
						leftValue -= $self.outerWidth()/2;
					}
					else if(config.horizontal_align == "right") {
						leftValue = parentWidth;
						leftValue -= $self.outerWidth();
						leftValue -= config.horizontal_offset;
					}
					else {
						horizontal_align_error = true;
						horizontal_align_error_message = "horizontal_align : choose a valid options.\n";
						error = true;
						leftValue =0;
					}
					 
					if(config.vertical_align == "top") {
						topValue = 0;
						topValue += config.vertical_offset;
					}
					else if(config.vertical_align == "middle") {
						topValue = parentHeight/2;
						topValue -= $self.outerHeight()/2;
					}
					else if(config.vertical_align == "bottom") {	
						topValue = parentHeight;
						topValue -= $self.outerHeight();
						topValue -= config.vertical_offset;
					}
					else {
						vertical_align_error = true;
						vertical_align_error_message = "vertical_align : choose a valid options.\n";
						error = true;
						topValue =0;
					}
				}
				if(leftValue <= 0){leftValue = 0;}
				if(topValue <= 0){topValue = 0;}
			
				if(config.axis == "xy") {
					$self.css({top: topValue, left: leftValue});
				}
				else if(config.axis == "y") {
					$self.css({top: topValue});
				}
				else if (config.axis == "x") {
					$self.css({left: leftValue});
				}
				else {
					axis_error = true;
					axis_error_message = "axis : choose a valid axis.\n";
					error = true;
				}
				if(error) {
					var textAlert = "JCSS_ALIGN Plugins : \n";
					if(horizontal_align_error) {
						textAlert += horizontal_align_error_message;
					}
					
					if(vertical_align_error) {
						textAlert += vertical_align_error_message;
					}
					
					if(axis_error) {
						textAlert += axis_error_message;
					}
					alert(textAlert);
				}
				return self;
			},
			image_load: function() {
				number_of_images_preloaded++;
				if(number_of_images_preloaded >= total_img_to_load && images_loaded != true){
					images_loaded = true;
					self.init();
				}
			}
		});
		if(self.tagName == "IMG") {
			var image = new Image();
			image.src = $self.attr('src');
			if (image.complete)        
			self.image_load();        
			else        
			image.onload = self.image_load;
		}
		else {
			if($self.find('img').exists()) {
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
				self.init();
			}
		}
		
		$(window).resize(function(){self.init();});
	}
	
	function viewport() {
		// the horror case
		if ($.browser.msie) {
			// if there are no scrollbars then use window.height
			var d = $(document).height(), w = $(window).height();
			return [
				window.innerWidth || 						// ie7+
				document.documentElement.clientWidth || 	// ie6  
				document.body.clientWidth, 					// ie6 quirks mode
				d - w < 20 ? w : d
			];
		} 
		// other well behaving browsers
		return [$(window).width(), $(window).height()];
	}
})(jQuery);