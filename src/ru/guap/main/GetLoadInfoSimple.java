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
import ru.guap.treeview.BurdenManager;

@WebServlet(description = "Get load info from DB by load ID", urlPatterns = { "/GetLoadInfoSimple" })
@MultipartConfig
public class GetLoadInfoSimple extends HttpServlet {
	private static final long serialVersionUID = 2L;

	private static Connection cnn;
	private static PreparedStatement psLoad;

	public GetLoadInfoSimple() {
		super();

		cnn = DBManager.getInstance().getConnection();

		try {

			psLoad = cnn.prepareStatement("SELECT `Group`, KindLoad, NameDisc, teachers_id, ValueG, ValueCO FROM kafedra.kaf43 WHERE id = ?;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int loadID = Integer.parseInt(request.getParameter("id"));
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		//PrintWriter out = response.getWriter();
		
		if (loadID <= 0) {
			response.getWriter().println("{ \"error\": true }");
		} else {
			try {
				psLoad.setInt(1, loadID);

				ResultSet res = psLoad.executeQuery();

				while (res.next()) {
					String group = res.getString(1);
					String kind = res.getString(2);
					kind = BurdenManager.getInstance().renameLoadKind(kind);
					String nameDisc = res.getString(3);
					int teacherID = res.getInt(4);
					String fio = "<�� ��������>";
					if (teacherID != 0) {
						fio = DBManager.getInstance().getTeacherById(teacherID);
					}

					int valueG = res.getInt(5);
					int valueC = res.getInt(6);
					
					PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
					
					String output = String.format("{ \"error\": false, "
							+ " \"group\": \"%s\", "
							+ " \"kindload\": \"%s\", "
							+ " \"namedisc\": \"%s\", "
							+ " \"fio\": \"%s\", "
							+ " \"valueg\": \"%s\", "
							+ " \"valuec\": \"%s\", "
							+ " \"valuetotal\": \"%s\" "
							+ "}", group, kind, nameDisc, fio, valueG, valueC, valueG + valueC);
					
					out.println(output);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}