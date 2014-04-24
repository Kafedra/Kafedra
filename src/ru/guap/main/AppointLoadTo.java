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

import nl.knaw.dans.common.dbflib.CorruptedTableException;
import nl.knaw.dans.common.dbflib.DbfLibException;
import nl.knaw.dans.common.dbflib.Field;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;
import nl.knaw.dans.common.dbflib.ValueTooLargeException;
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

		cnn = DBManager.getInstance().getConnection();

		try {
			psUpdateLoad = cnn.prepareStatement("UPDATE kafedra.kaf43 SET teachers_id = ? WHERE id = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int loadID = Integer.parseInt(request.getParameter("load_id"));
		int teacherID = Integer.parseInt(request.getParameter("teacher_id"));
		response.setContentType("text/plain; charset=UTF-8");

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
		
		if (loadID <= 0 || teacherID <= 0) {
			out.println("{ \"success\": false, \"reason\": \"you are an idiot\" }");
		} else {
			try {
				psUpdateLoad.setInt(1, teacherID);
				psUpdateLoad.setInt(2, loadID);
				
				psUpdateLoad.executeUpdate();

				out.println("{ \"success\": true }");
			} catch (SQLException e) {
				out.println("{ \"success\": false, \"reason\" : \"" + e.getMessage() + "\" }");
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
