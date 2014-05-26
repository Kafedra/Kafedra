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
		else{
			$(".cb").attr("checked",true);
		}
		$(".cb:last").trigger('change');	
	});	
	
	$(".cb").change(function (event) {
		if($(this).is(':checked')){
			$(this).parent().find(".cb").attr("checked",true);
		}else {
			$(this).parent().find(".cb").removeAttr("checked");				
		}
	});
	
	$( "#clearChecked" ).click(function( event ) {	
		$(".cb:checked").removeAttr("checked");
		$(".cb:last").trigger('change');
	});
	
	$( "#funcMenu" ).menu({position: {at: "left bottom"}});
});


