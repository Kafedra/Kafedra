<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%> 
    <div id="tabs" >
        <ul>
             <li><a href="#tabs-1">Весенний</a></li>
             <li><a href="#tabs-2">Осенний</a></li>
        </ul>
        <div id="tabs-1">
        <ul class="tree">
         	 <%@ include file="../tree_ves.jsp" %>
          </ul>
        </div>
        <div id="tabs-2">
        <ul class="tree">
          	<%@ include file="../tree_os.jsp" %>
          </ul>
	</div>
    </div>
    <table>
    	<tr>
    		<td>
    			<input id="checkAll" type="button" class="but" value="      Выбрать всё        "/>
    		</td>
    		<td>
    			<input id="clearChecked" type="button" class="but" value="Очистить выбранные"/>
    		</td>
    		
    	</tr>
    	<tr>
    		<td>
    			<input id="hideChecked" type="button" class="but" value="Скрыть выбранные"/>
    		</td>
    		<td>
    			<input id="showChecked" type="button" class="but" value="Раскрыть выбранные"/>
    		</td>
    	</tr>
    </table>
	
	
	