//Script for muli appointment in one click 
var groupArray;
var jsonGroupArray;

	//Groups to show on page
	function Group (){
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
	function checkedGroup(){ 
		var isMulti;
		var id;
		var discId;
		var streamId;		
	}
	
	
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val(); 	
		//getting teacher and appointing to array of groups
		ajaxAppoint(val,jsonGroupArray);
		//refreshing bar charts
		$('#percent').find('img').attr('src', '../PercentBarChart?'+Math.random());
		$('#time').find('img').attr('src', '../HoursBarChart?'+Math.random());
	});
		
	$(".cb").change(function (event) {
		if($(this).is(':checked')){			
			groupArray = new Array();	
			jsonGroupArray = new Array();
			$(".cb:checked").parent().find("a[group]").each(function(index,x){
				
				var group = new Group();
				var jsonGroup = new checkedGroup();
				//Group to send by json
				jsonGroup.id = $(x).attr('id');
				jsonGroup.isMulti = $(x).attr('ismulti');
				jsonGroup.discId = $(x).attr('discid');
				jsonGroup.streamId = $(x).attr('streamid');
				
				//Group to show on page
				group.discname = $(x).parent().parent().parent().parent().text();
				group.kindLoad = $(x).parent().parent().text();
				group.groupName = $(x).attr('group');
				group.valueG = $(x).attr('valueg');
				group.valueC= $(x).attr('valuec');
				group.total = $(x).attr('valueep');
				group.teacher = $(x).attr('teacherid'); //--
				group.id = $(x).attr('id');	
				group.isMulti = $(x).attr('ismulti');
				
				//adding groups to array
				jsonGroupArray.push(jsonGroup);
				groupArray.push(group);
			});
		}			
	});	