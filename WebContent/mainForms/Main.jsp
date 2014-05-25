<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="ru.guap.config.WebConfig" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Кафедра</title>
<link rel="stylesheet" href="../css/top_block_style.css">
<link rel="stylesheet" href="../css/main_style.css">
<link rel="stylesheet" href="../css/tree.css">
<link rel="stylesheet" href="../css/appointment_style.css">
<link rel="stylesheet" href="../css/jquery.dataTables.css">
<link rel="stylesheet" href="../css/hot-sneaks/jquery-ui-1.9.2.custom.min.css">
<link rel="stylesheet" href="../css/ProgressBar.css">
<script src="../js/jquery-1.8.3.js"></script>
<script src="../js/jquery-ui-1.9.2.custom.min.js"></script>
<script src="../js/jquery.json.min.js"></script>
<script src="../js/script.js"></script>
<script src="../js/multiApp.js"></script>
<script src="../js/tree.js"></script>
<script src="../js/painting.js"></script>
<script src="../js/appointment_scripts.js"></script>
<script src="../js/top_block_script.js"></script>
<script src="../js/ProgressBar.js"></script>
</head>
<body>
	<%@ include file="blocks/dialogs.jsp" %>
	<div class="container">
		<table>
		<tr>
			<td colspan=2 class="top">
				<%@ include file="blocks/top_block.jsp" %>
			</td>
		</tr>
		<tr>				
			<td rowspan=2 class="tree" >
			<fieldset style="vertical-align:top; horizontal-align:left;">
				<legend>Кафедра</legend>
				 <%@ include file="blocks/tree_block.jsp" %>
			</fieldset>	
			</td>		
			
			<td class="chart" >	
			<fieldset>
				<legend>Нагрузка</legend>
				<%@ include file="blocks/chart_block.jsp" %>
				<%@ include file="blocks/ProgressBar.jsp" %>
				<%@ include file="blocks/appointment_block.jsp" %>  
			</fieldset>	
            </td>
           </tr>
		</table>
	</div>
</body>
</html>