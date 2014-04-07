<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>	
	<style>
		.div{
		  id="tabs";
		  margin:0;
		  align
		}
	</style>
	<div id="tabs" >
  		<ul>
   			 <li id="showtab2"><a href="#">Весенний</a></li>
   			 <li id="showtab1"><a href="#">Осенний</a></li>
	    </ul>
		<div id="tabs-1" style="display: none; height=300; width=400; overflow-y: auto;">
		  <ul class="tree" id="tree">
		      <%@ include file="../tree_os.jsp" %>
		  </ul> <!-- tree -->
		</div>
		<div id="tabs-2" style="height=300; width=400; overflow-y: auto;">
		  <ul class="tree" id="tree">
		      <%@ include file="../tree_ves.jsp" %>
		  </ul> <!-- tree -->
		 </div>
	</div>
	
    <script type="text/javascript">
	    $('#showtab2').click(function() {
	        $('#tabs-2').show();
	        $('#tabs-1').hide();
	    });
	    
        $('#showtab1').click(function() {
            $('#tabs-1').show();
            $('#tabs-2').hide();
        });	    
    </script>	