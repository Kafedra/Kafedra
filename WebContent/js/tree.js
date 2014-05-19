var globalSelectedLoadID = 0;
var globalIsMulti = false;
var globalStreamID = 0;
var globalDiscID = 0;

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

//Get teacher's load info by ajax-GET query
function ajaxLoad(id) {	
	$.getJSON("../GetLoadInfoSimple", {"id": id, "random" : Math.random()*99999}).done(function( responseObject ) {
		$('#app-NameDisc').text(responseObject.namedisc);
		$('#app-KindLoad').text(responseObject.kindload);
		$('#app-Group').text(responseObject.group);
		$('#app-Teacher').text(responseObject.fio);

		$('#app-ValueG').text(responseObject.valueg);
		$('#app-ValueC').text(responseObject.valuec);
		$('#app-Total').text(responseObject.valuetotal);

		globalIsMulti = false;
		globalSelectedLoadID = id;
	}).fail(function( jqxhr, textStatus, error ) {
		var err = textStatus + ", " + error;
		alert("Request Failed: " + err);
	});
}

//Get teacher's load info by ajax-GET query
function ajaxLoadMulti(id, streamid, discid) {	
	$.getJSON("../GetLoadInfoMulti", { "streamid": streamid, "discid": discid, "random" : Math.random()*99999 }).done(function( responseObject ) {
		$('#app-NameDisc').text(responseObject.namedisc);
		$('#app-KindLoad').text(responseObject.kindload);
		$('#app-Group').text(responseObject.group);
		$('#app-Teacher').text(responseObject.fio);

		$('#app-ValueG').text(responseObject.valueg);
		$('#app-ValueC').text(responseObject.valuec);
		$('#app-Total').text(responseObject.valuetotal);

		globalIsMulti = true;
		globalSelectedLoadID = id;
		globalStreamID = streamid;
		globalDiscID = discid;
	}).fail(function( jqxhr, textStatus, error ) {
		var err = textStatus + ", " + error;
		alert("Request Failed: " + err);
	});
}

//Set teacher's load info by ajax-GET query
function ajaxAppoint(teacher_id,selectedLoadID) {
	if (globalIsMulti) {
		if (globalDiscID == 0 || globalStreamID == 0) {
			alert("Группа не выбрана!");
			return;
		}
		
		$.getJSON("../AppointLoadToMulti", {"discid": globalDiscID, "streamid": globalStreamID, "teacher_id": teacher_id, "random" : Math.random()*99999}).done(function( response ) {

			if (response.success) {
				ajaxLoadMulti(globalDiscID + 23 * globalStreamID, globalStreamID, globalDiscID);
				$('#progressbar').trigger('refresh');
			} else {
				alert(response.reason + " - error!");
			}
		}).fail(function( jqxhr, textStatus, error ) {
			var err = textStatus + ", " + error;
			alert("Request Failed: " + err);
		});
	} else {
		if (selectedLoadID == 0) {
			alert("Группа не выбрана!");
			return;
		}
		
		$.getJSON("../AppointLoadTo", {"load_id": selectedLoadID, "teacher_id": teacher_id, "random" : Math.random()*99999}).done(function( response ) {
			if (response.success) {
				ajaxLoad(globalSelectedLoadID);
				$('#progressbar').trigger('refresh');
			} else {
				alert(response.reason + " - error!");
			}
		}).fail(function( jqxhr, textStatus, error ) {
			var err = textStatus + ", " + error;
			alert("Request Failed: " + err);
		});
	}
}
