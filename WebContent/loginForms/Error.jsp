<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<html>
    <head>  
   		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	     
        <title>Error Page</title>        	
		<link rel="stylesheet" href="../css/style.css">
		<link rel="icon"  type="../loginImg/x-icon">
		<link rel="shortcut icon" type="../loginImg/x-icon">	
    </head>
    <style>
   div {    
    margin-bottom: 5px; /* Отступ снизу */
   }  
   #center { 
   	top: 50%; /* Отступ в процентах от верхнего края окна */
	left: 50%; /* Отступ в процентах от левого края окна */
	width: 450px; /* Ширина блока */
	height: 450px; /* Высота блока */
	position: absolute; /* Абсолютное позиционирование блока */
	margin-top: -225px; /* Отрицательный отступ от верхнего края страницы, должен равняться половине высоты блока со знаком минус */
	margin-left: -225px; /* Отрицательный отступ от левого края страницы, должен равняться половине высоты блока со знаком минус */ }
   .content {
   font-size:50px;
   
   }
   A {
    color: #FF4500; /* Цвет ссылок */
   }
   A:visited {
    color: #E9967A; /* Цвет посещенных ссылок */
   }
   A:active {
    color: #FF0000; /* Цвет активных ссылок */
   }
  </style>    
    	<body link="red" vlink="#cecece" alink="#ff0000">
    	<div id="center"><div class="content"><a href="../index.jsp">Authorization failed</a></div></div>
    </body>
</html>