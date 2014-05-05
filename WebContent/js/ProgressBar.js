$(function() {
	 var progressbar = $( "#progressbar" ),
	 	progressLabel = $( ".progress-label" );
	 
	 progressbar.progressbar({
		 value: false,
		 change: function() {
			 progressLabel.text( progressbar.progressbar( "value" ) + "%" );
		 },
		 complete: function() {
			 progressLabel.text( "Complete!" );
		 }
	 });
	 	
	 progressbar.on('refresh', refreshProgress);
	 progressbar.trigger('refresh');
	 			
	 function refreshProgress(event){
		$.getJSON( "../GetProgress", function(data) {
			var $pb = $(event.target);
			var value = (data.appointed/data.allRecords)*100;
			$pb.progressbar("value", Math.round(value*10)/10);
			})
		.fail(function() {
		    console.log( "error" );
		  });
	}
	 
});



	 

 
