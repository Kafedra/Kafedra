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
import ru.guap.treeview.Discipline;
import ru.guap.treeview.GroupLoadItem;
import ru.guap.treeview.GroupStream;

@WebServlet(description = "Appoints specified load stream to specified teacher by specified stream id and discipline id", urlPatterns = { "/AppointLoadToMulti" })
@MultipartConfig
public class AppointLoadToMulti extends HttpServlet {
	private static final long serialVersionUID = 2L;

	private static Connection cnn;
	private static PreparedStatement psUpdateLoad;

	public AppointLoadToMulti() {
		super();

		cnn = DBManager.getInstance().getConnection();

		try {
			psUpdateLoad = cnn.prepareStatement("UPDATE kafedra.kaf43 SET teachers_id = ? WHERE id = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int streamId = Integer.parseInt(request.getParameter("streamid"));
		int discId = Integer.parseInt(request.getParameter("discid"));
		int teacherId = Integer.parseInt(request.getParameter("teacher_id"));
		response.setContentType("text/plain; charset=UTF-8");

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
		
		if (streamId <= 0 || discId < 0) {
			out.println("{ \"success\": false, \"reason\": \"streamId or discId is zero\" }");
		} else {
			try {
				GroupStream s = BurdenManager.getInstance().getStreamForDiscAndId(streamId, discId);
				
				if (s == null) {
					response.getWriter().println("{ \"error\": true, \"reason\": \"there is no discipline for this streamid\" }");
					return;
				}
				
				for (GroupLoadItem item : s.getItems()) {
					appointTeacherTo(item.getId(), teacherId);
					
					item.setAppointed((teacherId != 0));
					item.setTeacherId(teacherId);
				}

				out.println("{ \"success\": true }");
			} catch (SQLException e) {
				out.println("{ \"success\": false, \"reason\" : \"" + e.getMessage() + "\" }");
				e.printStackTrace();
			}
		}
	}

	private void appointTeacherTo(int loadID, int teacherID) throws SQLException {
		if (teacherID == 0) {
			psUpdateLoad.setNull(1, Types.INTEGER);
		} else {
			psUpdateLoad.setInt(1, teacherID);
		}
		
		psUpdateLoad.setInt(2, loadID);
		
		psUpdateLoad.executeUpdate();		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
