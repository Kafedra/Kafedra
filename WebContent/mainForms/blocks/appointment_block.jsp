<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ru.guap.dao.DBManager" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
 </head>
    <body>
        <table>
            <tr>
                <td>Дисциплина: <span id="app-NameDisc"></span></td>            
            </tr>
            <tr>
                <td>Тип занятий: <span id="app-KindLoad"></span></td>
            </tr>
            <tr>
                <td>Группа: <span id="app-Group"></span></td>
            </tr>
            <tr>
                <td>Преподаватель: <span id="app-Teacher"></span></td>
            </tr>
            <tr>
                <td><%@ include file="table_block.jsp" %></td> 
            </tr>
                        
            <tr>
                <td>                            
                <fieldset>                                                      
                <legend>Область заполнения данных:</legend>
                <table>
                    <tr>
                        <td>Выбор преподавателя:</td>   
                            <td>
                                <select id="combobox">
                                <sql:setDataSource var="ds" driver="com.mysql.jdbc.Driver" url="<%=DBManager.URL %>" user="<%=DBManager.DB_LOGIN %>" password="<%=DBManager.DB_PASS %>"/>
                                
                                <sql:query dataSource="${ds}" sql="SELECT * FROM kafedra.teachers;" var="result" />                     
                                <c:forEach var="row" items="${result.rows}">
                                    <option value="${row.id}"><c:out value="${row.fio}"/></option>
                                </c:forEach>
                                </select> <input id="btnappoint" type="button" value="Назначить"/>
                            </td>     
                    </tr>
                </table>            
                </fieldset>                     
                </td>                   
        </table>        
    </body>
</html>