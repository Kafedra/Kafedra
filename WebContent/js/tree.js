var globalSelectedLoadID = 0;

$.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});

$(function() {
	$('.tree a').on('click', function(event) {
		event.preventDefault();
		$ul = $(this).siblings('ul');
		if ($ul.css('display') == 'none')
			$ul.show();
		else
			$ul.hide();
	});
});

function toggle(X){
}

// Get teacher's load info by ajax-GET query
function ajaxLoad(id) {
	$.getJSON("../GetLoadInfo", {"id": id, "random" : Math.random()*99999}).done(function( responseObject ) {
        $('#app-NameDisc').text(responseObject.namedisc);
        $('#app-KindLoad').text(responseObject.kindload);
        $('#app-Group').text(responseObject.group);
        $('#app-Teacher').text(responseObject.fio);
        
        globalSelectedLoadID = id;
	}).fail(function( jqxhr, textStatus, error ) {
		var err = textStatus + ", " + error;
		alert("Request Failed: " + err);
	});
}

//Get teacher's load info by ajax-GET query
function ajaxAppoint(teacher_id) {
	if (globalSelectedLoadID == 0) {
		alert("Группа не выбрана!");
		return;
	}
	$.getJSON("../AppointLoadTo", {"load_id": globalSelectedLoadID, "teacher_id": teacher_id, "random" : Math.random()*99999}).done(function( response ) {
        if (response.success) {
        	ajaxLoad(globalSelectedLoadID);
    		$('#progressbar').trigger('refresh');
        } else {
        	alert(globalSelectedLoadID + " - error!");
        }
	}).fail(function( jqxhr, textStatus, error ) {
		var err = textStatus + ", " + error;
		alert("Request Failed: " + err);
	});
}
