<%@ page language="java" contentType="text/html" pageEncoding="cp1251"%>
<%@page import="ru.guap.config.WebConfig" %>
<div id="dialog" title="�������� LONG ����� �������� �����">
	<form action="<%=WebConfig.JSP_PREFIX %>/dbfupload" method="post" enctype="multipart/form-data">
        <input type="file" name="dbf"><br>
        <input type="submit" name="sumbit">
    </form>
</div>

<div id="dialog_report" title="�������� ������">
    <form action="<%=WebConfig.JSP_PREFIX %>/GetReport" method="get" enctype="multipart/form-data">
        <select name="type">
            <option value="0">������� �������</option>
            <option value="1">�������� �������</option>
            <option value="2">������� + �������� ��������</option>
        </select>
        <input type="submit" name="submit" value="������� �����">
    </form>
</div>