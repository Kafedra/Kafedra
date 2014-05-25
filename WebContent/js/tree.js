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
			ulOpen($ul);
		else
			ulClose($ul);
	});
	
	
	$( "#showChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').each(function(){
			ulOpen($(this));
		});
	});
	
	$( "#hideChecked" ).click(function( event ) {			
		$(".cb:checked").parent().find('ul').each(function(){
			ulClose($(this));			
		});
	});
	
	function ulOpen($ul){
		$ul.show();
		$ul.siblings(".plus").attr("src","../treeImg/minus.jpg");
	}
	
	function ulClose($ul){
		$ul.hide();
		$ul.siblings(".plus").attr("src","../treeImg/plus.jpg");
	}
});

function toggle(X){
}
/*
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

//Set teacher's load info by ajax-GET query*/
(function ($) {
	 
    $.postJSON = function (url, data) {
 
        var o = {
            url: url,
            type: "POST",
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : data,
        };
 
        return $.ajax(o);
    };
 
} (jQuery));

function ajaxAppoint(teacher_id, jsonGroups) {
		if (jsonGroups == null) {
			alert("Группа не выбрана!");
			return;
		}
				
		var jsonString = $.toJSON(jsonGroups);
		
		$.postJSON("../AppointLoadTo", {"data": jsonString, "teacher_id": teacher_id, "random" : Math.random()*99999}).done(function( response ) {
			if (response.success) {
				$('#progressbar').trigger('refresh');
				
				// Repaint appointed items
				response.appointed.forEach(function(entry) {
					$('#' + entry).children('a').removeClass('notappointed').addClass('appointed');
					$('#' + entry).children('a').attr('teacher', response.teacher);
				});
				
				paintStart();
				
			} else {
				alert(response.reason + " - error!");
			}
		}).fail(function( jqxhr, textStatus, error ) {
			var err = textStatus + ", " + error;
			alert("Request Failed: " + err);
		});
	}

