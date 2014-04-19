(function () {
	$(document).ready(function(){
		$( "#tabs" ).tabs();
		$( "a.button" )
	      .click(function( event ) {
	        event.preventDefault();
	      });
	});
})();

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
	 $( "#opener" ).click(function() {
		 $( "#dialog" ).dialog( "open" );
		 });
		 });


$(function() {  //Для кнопок
	$( ".but" )
	.button()
	.click(function( event ) {
	event.preventDefault();
	});
});