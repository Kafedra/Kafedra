$(function() {
	$( "#tabs" ).tabs();
	
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
	 
	$( "#opener" ).click(function() {
		$( "#dialog" ).dialog( "open" );
	});

	$( ".but" ).button().click(function( event ) {
		event.preventDefault();
	});
	
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val();
		ajaxAppoint(val);
	});
	
	$( "#show" ).click(function( event ) {
		$("a.notappointed,a.semiappointed").siblings('ul').show();
	});	
});


