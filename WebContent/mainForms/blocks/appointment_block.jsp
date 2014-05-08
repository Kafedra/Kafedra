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
                <td class="app">Дисциплина: <span id="app-NameDisc"></span></td>            
            </tr>
            <tr>
                <td class="app">Вид занятия: <span id="app-KindLoad"></span></td>
            </tr>
            <tr>
                <td class="app">Группа (поток): <span id="app-Group"></span></td>
            </tr>
            <tr>
                <td class="app">Количество часов:</td>
            </tr>
            <tr>
                <td class="app">Бюджет: <span id="app-ValueG"></span></td>
            </tr>
            <tr>
                <td class="app">Контракт: <span id="app-ValueC"></span></td>
            </tr>
            <tr>
                <td class="app">Итого: <span id="app-Total"></span></td>
            </tr>
            <tr>
                <td class="app">Преподаватель: <span id="app-Teacher"></span></td>
            </tr>                        
            <tr>
                <td>                            
                <fieldset>                                                      
                <legend>Область заполнения данных:</legend>
                <table>
                    <tr>
                        <td>Выбор преподавателя:</td>   
                            <td>
                                <select id="combobox" style="width:3in">
                                <option value="0">-</option>
                                <sql:setDataSource var="ds" driver="com.mysql.jdbc.Driver" url="<%=DBManager.URL %>" user="<%=DBManager.DB_LOGIN %>" password="<%=DBManager.DB_PASS %>"/>
                                <sql:query dataSource="${ds}" sql="SELECT * FROM kafedra.teachers;" var="result" />                     
                                <c:forEach var="row" items="${result.rows}">
                                    <option value="${row.id}"><c:out value="${row.fio}"/></option>
                                </c:forEach>
                                </select>                               
                            </td>     
                            <td>
                            	 <input id="btnappoint" type="button" class="but" value="Назначить"/>
                            </td>
                    </tr>
                </table>            
                </fieldset>                     
                </td>                   
        </table>        
    </body>
</html>