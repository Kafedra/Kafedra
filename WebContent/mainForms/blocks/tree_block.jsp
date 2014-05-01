<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%> 
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
    <input id="hide" type="button" class="but" value="Скрыть всё"/>
	<input id="showChecked" type="button" class="but" value="Раскрыть выбранные"/>
	<input id="hideChecked" type="button" class="but" value="Скрыть выбранные"/>
	<input id="clearChecked" type="button" class="but" value="Очистить выбранные"/>