//Script for muli appointment in one click 
var groupArray;

$(function() {
	
	function Group (){
		var discName;
		var kindLoad;
		var groupName;
		var valueG;
		var valueC;
		var total;
		var teacher;
		var id;		
	}	
	
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val(); 	
		
		groupArray.forEach(function(x){
			ajaxAppoint(val,x.id);
			globalSelectedLoadID=x.id;
		});
		
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
	});
		
	$(".cb").change(function (event) {
		if($(this).is(':checked')){			
			groupArray = new Array();			
			$(".cb:checked").parent().find("a[group]").each(function(index,x){
				var group = new Group();				
				$.getJSON("../GetLoadInfoSimple", {"id": $(x).attr('id'), "random" : Math.random()*99999}).done(function( responseObject ) {
					
					group.discName = responseObject.namedisc;
					group.kindLoad = responseObject.kindload;
					group.groupName = responseObject.group;
					group.valueG = responseObject.valueg;
					group.valueC = responseObject.valuec;
					group.total = responseObject.valuetotal;
					group.teacher = responseObject.fio;
					group.id = $(x).attr('id');		
					groupArray.push(group);	
				}).fail(function( jqxhr, textStatus, error ) {
					var err = textStatus + ", " + error;
					alert("Request Failed: " + err);
				});				
			});
		}			
	});	
});