package ru.guap.progressbar;

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

@WebServlet(description = "Get count of all records and appointed records", urlPatterns = { "/GetProgress" })
@MultipartConfig

public class ProgressBar extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Connection cnn;
	private static PreparedStatement psAll,psApp;
	
	public ProgressBar(){
		super();
		cnn = DBManager.getInstance().getConnection();
	
		try {
			psAll = cnn.prepareStatement("SELECT COUNT(id) FROM kafedra.kaf43;");
			psApp = cnn.prepareStatement("SELECT COUNT(teachers_id) FROM kafedra.kaf43;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		try{
			PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
			int all=2;
			int app=2;
			ResultSet allRes = psAll.executeQuery();
			ResultSet appRes = psApp.executeQuery();
						
			while(allRes.next())
				 all=allRes.getInt(1);
			
			while(appRes.next())
				app=appRes.getInt(1);
			
			String output = String.format("{ \"error\": false,"
					+ " \"appointed\": %s,"
					+ " \"allRecords\": %s"
					+ "}",app ,all );
			
			out.println(output);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		}
}
	
	

