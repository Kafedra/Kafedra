<%@ page language="java" contentType="text/html" pageEncoding="cp1251"%>
<%@page import="ru.guap.config.WebConfig" %>
<div id="dialog" title="Загрузка LONG файла учебного плана">
	<form action="<%=WebConfig.JSP_PREFIX %>/dbfupload" method="post" enctype="multipart/form-data">
        <input type="file" name="dbf"><br>
        <input type="submit" name="sumbit">
    </form>
</div>

<div id="dialog_report" title="Загрузка отчёта">
    <form action="<%=WebConfig.JSP_PREFIX %>/GetReport" method="get" enctype="multipart/form-data">
        <select name="type">
            <option value="0">Осенний семестр</option>
            <option value="1">Весенний семестр</option>
            <option value="2">Осенний + Весенний семестры</option>
        </select>
        <input type="submit" name="submit" value="Скачать отчет">
    </form>
</div>