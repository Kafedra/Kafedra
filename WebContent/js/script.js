$(function() {
	$( "#tabs" ).tabs();
	$( "#histo" ).tabs();
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
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
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
	$( "#clearChecked" ).click(function( event ) {			
		$(".cb:checked").removeAttr("checked");
	});
	
	$( "#funcMenu" ).menu({position: {at: "left bottom"}});
});


