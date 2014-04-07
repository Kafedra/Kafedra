$(function(){
    $("#table").dataTable({
	  "aaData":[
	            ["4236","блла","блла","блла"],
	            ["2323","блла","блла","блла"],
	            ["1111","блла","блла","блла"],
	          ],
	          "aoColumnDefs":[{
	                "sTitle":"Группа"
	              , "aTargets": [ "group_name" ]
	          },{
	                "aTargets": [ 1 ]
	              , "bSortable": true
	              
	          },{
	                "aTargets":[ 3 ]	              
	          }]
	        });
  });