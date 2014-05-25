//Script for muli appointment in one click 
var groupArray;
var jsonGroupArray;
$(function() {
	//struct to show
	function TrueGroup () {
		this.discnames = [];
		this.kindloads = [];
		this.groupNames = [];
		this.teacher = [];
		
	}
	
	//Groups to show on page
	function Group () {
		var discname;
		var kindLoad;
		var groupName;
		var valueG;
		var valueC;
		var total;
		var teacher;
		var id;	
		var isMulti;
	}
	
	//Groups to send by json
	function checkedGroup() { 
		var isMulti;
		var id;
		var discId;
		var streamId;		
	}	
	
	//appointing by click
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val(); 	
		//getting teacher and appointing to array of groups
		ajaxAppoint(val,jsonGroupArray);
		//refreshing bar charts
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
		
	});
	
	$("#tabs").change('.cb', function (event) {
		trueGroups = new TrueGroup();
		
		//refreshing 
		$('#app-NameDisc').text('');
		$('#app-KindLoad').text('');
		$('#app-Group').text('');
		
		if($(this).is(':checked')){
			
		}
		else {			
			jsonGroupArray = new Array();
			groupArray = new Array();
			$(".cb:checked").parent().find("a[group]").each(function(index,x) {				
				var group = new Group();
				var jsonGroup = new checkedGroup();
				//Group to send by json
				jsonGroup.id = $(x).attr('id');
				jsonGroup.isMulti = $(x).attr('ismulti');
				jsonGroup.discId = $(x).attr('discid');
				jsonGroup.streamId = $(x).attr('streamid');
				
				//Group to show on page
				group.discname =  $(x).parent().parent().parent().parent().parent().parent().parent().children('a').text();
				group.kindLoad =  $(x).parent().parent().parent().children('a').text();
				group.groupName = $(x).attr('group');
				group.valueG =	$(x).attr('valueg');
				group.valueC= 	$(x).attr('valuec');
				group.total =	$(x).attr('valueep');
				group.teacher = $(x).attr('teacherid'); //--
				group.id = 		$(x).attr('id');	
				group.isMulti = $(x).attr('ismulti');
				
				//remove repeatings
				if (trueGroups.discnames.indexOf(group.discname) >= 0 ){ 					
				}
				else
					trueGroups.discnames.push(group.discname);
				
				if(trueGroups.kindloads.indexOf(group.kindLoad) >= 0){
				}
				else
					trueGroups.kindloads.push(group.kindLoad);
				
				if(trueGroups.groupNames.indexOf(group.groupName) >= 0){
					
				}
				else 
					trueGroups.groupNames.push(group.groupName);
				
				//adding groups to array
				jsonGroupArray.push(jsonGroup);
				groupArray.push(group);				
			});
		}
		
		//Showig info on the page
		trueGroups.discnames.forEach(function(x){
			$('#app-NameDisc').append(x + " ");
		});
			
		trueGroups.kindloads.forEach(function(x){
			$('#app-KindLoad').append(x + " ");
		});
		
		trueGroups.groupNames.forEach(function(x){
			$('#app-Group').append(x + " ");			
		});
		/*
		 * 
		$('#app-NameDisc').text(responseObject.namedisc);
		$('#app-KindLoad').text(responseObject.kindload);
		$('#app-Group').text(x.groupName + " ");
		$('#app-Teacher').text(responseObject.fio);

		$('#app-ValueG').text(responseObject.valueg);
		$('#app-ValueC').text(responseObject.valuec);
		$('#app-Total').text(responseObject.valuetotal);
		*/
	});	
});