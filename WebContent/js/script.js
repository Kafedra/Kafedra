$(function() {
	$( "#tabs" ).tabs();
	$( "#histo" ).tabs();
	$( "#load,#dialog_report,#norm").dialog({
		minHeight: 200,
		minWidth : 400,
		resizable:false,
		autoOpen: false,
		show: {
			effect: "blind",
			duration: 1000
		},
		hide: {
			effect: "fade",
			duration: 1000
		}
	}); 
	
	$( "#opener" ).click(function() {
		$( "#load" ).dialog( "open" );
	});

	$( "#opener_report" ).click(function() {
		$( "#dialog_report" ).dialog( "open" );
	});	
	
	$( "#norm_edit" ).click(function() {
		$( "#norm" ).dialog( "open" );
	});

	
	$( ".but" ).button().click(function( event ) {
		event.preventDefault();
	});
	
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val();
		ajaxAppoint(val);
		
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random()); // Strange bug workaround: duplicate chart loading
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
	});
	
	// Reloading of charts images by click
	$( "#percent ").click(function (event) {
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
	});
	
	$( "#time ").click(function (event) {
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
	});
	
	$( "#show" ).click(function( event ) {
		$("a.notappointed,a.semiappointed,a.appointed").siblings('ul').hide();
		$("a.notappointed,a.semiappointed").siblings('ul').show();		
	});	
	
	$( "#hide" ).click(function( event ) {
		$("a.notappointed,a.semiappointed,a.appointed").siblings('ul').hide();		
	});	
	
	$( "#checkAll" ).click(function( event ) {
		if($(".cb").prop("checked")){
			$(".cb").attr("checked",false);			
		}
		else
			$(".cb").attr("checked",true);			
	});	
	
	$( "#showChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').show();
	});
	
	$( "#hideChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').hide();
	});
	
	$(".cb").change(function (event) {
		$(this).find(".cb").attr("checked",true);
	});
	
	$( "#clearChecked" ).click(function( event ) {			
		$(".cb:checked").removeAttr("checked");
	});
	
	$( "#funcMenu" ).menu({position: {at: "left bottom"}});
});


