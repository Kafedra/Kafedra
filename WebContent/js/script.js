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

	$( "#opener_report" ).click(function() {
		$( "#dialog_report" ).dialog( "open" );
	});	
	
	$( ".but" ).button().click(function( event ) {
		event.preventDefault();
	});
	
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val();
		ajaxAppoint(val);
	});
	
	$( "#show" ).click(function( event ) {
		$("a.notappointed,a.semiappointed,a.appointed").siblings('ul').hide();
		$("a.notappointed,a.semiappointed").siblings('ul').show();		
	});	
	$( "#hide" ).click(function( event ) {
		$("a.notappointed,a.semiappointed,a.appointed").siblings('ul').hide();		
	});	
	
	$( "#showChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').show();
	});
	
	$( "#hideChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').hide();
	});
	
	$( "#funcMenu" ).menu({position: {at: "left bottom"}});
});


