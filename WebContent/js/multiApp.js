//Script for muli appointment in one click 
var groupArray;
var jsonGroupArray;
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
function CheckedGroup() { 
	var isMulti;
	var id;
	var discId;
	var streamId;		
}

$(function() {
	//appointing by click
	$( "#btnappoint" ).click(function( event ) {
		var val = $('#combobox option:selected').val(); 	
		//getting teacher and appointing to array of groups
		ajaxAppoint(val, jsonGroupArray);
		
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
		$('#app-Teacher').text('');
		
		if(!$(this).is(':checked')){			
			jsonGroupArray = new Array();
			groupArray = new Array();
			
			$(".cb:checked").parent().find("a[group]").each(function(index,x) {				
				var group = new Group();
				var jsonGroup = new CheckedGroup();
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
				group.teacher = $(x).attr('teacher'); 
				group.id = 		$(x).attr('id');	
				group.isMulti = $(x).attr('ismulti');
				
				//remove repeatings
				if (trueGroups.discnames.indexOf(group.discname) < 0 )					
					trueGroups.discnames.push(group.discname);
				
				if(trueGroups.kindloads.indexOf(group.kindLoad) < 0)
					trueGroups.kindloads.push(group.kindLoad);
				
				if(trueGroups.groupNames.indexOf(group.groupName) < 0)
					trueGroups.groupNames.push(group.groupName);

				if(trueGroups.teacher.indexOf(group.teacher) < 0)
					trueGroups.teacher.push(group.teacher);				
				
				//adding groups to array
				jsonGroupArray.push(jsonGroup);
				groupArray.push(group);				
			});
		}
		
		//Show info on the page
		trueGroups.discnames.forEach(function(x){
			$('#app-NameDisc').append(x + " ");
		});
			
		trueGroups.kindloads.forEach(function(x){
			$('#app-KindLoad').append(x + " ");
		});
		
		trueGroups.groupNames.forEach(function(x){
			$('#app-Group').append(x + " ");			
		});
		
		var valueG = 0;
		groupArray.forEach(function(x){
			valueG += +x.valueG;
						
		});
		$('#app-ValueG').text(valueG);
		
		var valueC = 0;
		groupArray.forEach(function(x){
			valueC += +x.valueC;	
		});		
		
		$('#app-ValueC').text(valueC);
		$('#app-Total').text(+valueG + +valueC);		
		
		trueGroups.teacher.forEach(function(x){
			$('#app-Teacher').append(x + " ");
		});		
	});	
});