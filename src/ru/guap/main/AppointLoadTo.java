package ru.guap.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Format;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import nl.knaw.dans.common.dbflib.CorruptedTableException;
import nl.knaw.dans.common.dbflib.DbfLibException;
import nl.knaw.dans.common.dbflib.Field;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;
import nl.knaw.dans.common.dbflib.ValueTooLargeException;
import ru.guap.appointment.AppointmentsManager;
import ru.guap.config.WebConfig;
import ru.guap.dao.DBManager;
import ru.guap.dao.dbf.DBFConverter;

@WebServlet(description = "Appoints specified load to specified teacher", urlPatterns = { "/AppointLoadTo" })
@MultipartConfig
public class AppointLoadTo extends HttpServlet {
	private static final long serialVersionUID = 2L;

	private static Connection cnn;
	private static PreparedStatement psUpdateLoad;

	public AppointLoadTo() {
		super();


	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonData = request.getParameter("data");
		Integer teacherId = Integer.valueOf(request.getParameter("teacher_id"));
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
		
		if (jsonData.isEmpty() || teacherId < 0) {
			out.println("{ \"success\": false, \"reason\": \"invalid json data\" }");
		} else {
			JSONParser parser = new JSONParser();
			JSONArray appointed = new JSONArray();
			
			try {
				JSONArray ja = (JSONArray) parser.parse(jsonData);
				
				for (Object object : ja) {
					JSONObject jo = (JSONObject) object;
					
					boolean isMulti = Boolean.valueOf((String) jo.get("isMulti"));
					
					if (isMulti) {
						int streamId = Integer.valueOf((String) jo.get("streamId"));
						int discId = Integer.valueOf((String) jo.get("discId"));
						int appointedId = AppointmentsManager.getInstance().appointTeacherToStream(streamId, discId, teacherId);
						
						if (appointedId != 0) {
							appointed.add(appointedId);
						}
					} else {
						int itemId = Integer.valueOf((String) jo.get("id"));
						AppointmentsManager.getInstance().appointTeacherToItem(itemId, teacherId);
						
						appointed.add(itemId);
					}
				}				
			} catch (ParseException e) {
				out.println("{ \"success\": false, \"reason\": \"json data corrupted\" }");
				e.printStackTrace();
				
				return;
			} catch (SQLException e) {
				out.println("{ \"success\": false, \"reason\": \"SQL error\" }");
				e.printStackTrace();
			}
			
			JSONObject joo = new JSONObject();
			joo.put("success", "true");
			joo.put("appointed", appointed);
			try {
				joo.put("teacher", DBManager.getInstance().getTeacherById(teacherId));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.println(joo.toJSONString());
		}		
	}
}
