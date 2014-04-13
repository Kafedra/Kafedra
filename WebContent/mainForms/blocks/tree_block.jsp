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
             <li><a href="#tabs-1">Весенний</a></li>
             <li><a href="#tabs-2">Осенний</a></li>
        </ul>
        <div id="tabs-1">
        <ul class="tree">
          <%@ include file="../tree_os.jsp" %>
          </ul>
        </div>
        <div id="tabs-2">
        <ul class="tree">
          <%@ include file="../tree_ves.jsp" %>
          </ul>
	</div>
    </div>