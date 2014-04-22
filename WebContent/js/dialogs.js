$(function() {
	$( "#dialog" ).dialog({
		autoOpen: false,
		show: {
			effect: "blind",
			duration: 1000
		},
		hide: {
			effect: "explode",
			duration: 1000
		}
	}); 
	
	$( "#dialog_report" ).dialog({
		autoOpen: false,
		show: {
			effect: "blind",
			duration: 1000
		},
		hide: {
			effect: "explode",
			duration: 1000
		}
	}); 	
	
	$( "#opener" ).click(function() {
		$( "#dialog" ).dialog( "open" );
	});
	
	$( "#report_opener" ).click(function() {
		$( "#dialog" ).dialog( "open" );
	});
});