<%@page import="ru.guap.config.WebConfig" %>
<div id="dialog" title="Load to Database">
	<form action="<%=WebConfig.JSP_PREFIX %>/dbfupload" method="post" enctype="multipart/form-data">
        <input type="file" name="dbf"><br>
        <input type="submit" name="sumbit">
    </form>
</div>