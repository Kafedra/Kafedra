$(function() {
	$( "#dialog" ).dialog({
		autoOpen: false,
		minHeight: 200,
		minWidth : 400,
		maxHeight : 200,
		maxWidth : 400,
		show: {
			effect: "blind",
			duration: 1000
		},
		hide: {
			effect: "slideUp",
			duration: 1000
		}
	}); 	
});