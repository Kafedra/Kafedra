<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>Авторизация</title>
		<link rel="stylesheet" href="css/login_style.css">
		<link rel="icon"  type="loginImg/x-icon">
		<link rel="shortcut icon" type="loginImg/x-icon">	
	</head>
	<body>
  		<form method="post" action="loginForms/LoginCheck.jsp" class="login">
    	<p>
      		<label for="login">Имя:</label>
    	    <input type="text" name="username" id="login" value="name">
   		 </p>
   		 <p>
      		<label for="password">Пароль:</label>
     	    <input type="password" name="password" id="password" value="pass">
    	</p>

    <p class="login-submit">
      <button type="submit" class="login-button" value="Submit">Войти</button>
    </p>

    <p class="forgot-password"><a href="index.html">Забыли пароль?</a></p>    
  </form>
</body>
</html>
