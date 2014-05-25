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
import ru.guap.treeview.Discipline;
import ru.guap.treeview.GroupLoadItem;
import ru.guap.treeview.GroupStream;

@WebServlet(description = "Get load info from DB by stream and discipline ID", urlPatterns = { "/GetLoadInfoMulti" })
@MultipartConfig
public class GetLoadInfoMulti extends HttpServlet {
	private static final long serialVersionUID = 2L;

	private static Connection cnn;
	private static PreparedStatement psLoad;

	public GetLoadInfoMulti() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int streamId = Integer.parseInt(request.getParameter("streamid"));
		int discId = Integer.parseInt(request.getParameter("discid"));

		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		//PrintWriter out = response.getWriter();

		if (streamId <= 0 || discId <= 0) {
			response.getWriter().println("{ \"error\": true }");
		} else {
			try {
				GroupStream s = BurdenManager.getInstance().getStreamForDiscAndId(streamId, discId);
				if (s == null) {
					response.getWriter().println("{ \"error\": true, \"reason\": \"there is no discipline for this streamid\" }");
					return;
				}

				String group = "?", kind = "?", nameDisc = "?", fio = "<не назначен>";
				int teacherId = 0;
				int valueEP = 0, valueG = 0, valueC = 0;
				
				boolean isInfoSet = false;
				boolean isAppointed = true;
				group = s.getGroupStringList();
				
				for (GroupLoadItem item : s.getItems()) {
					if (!isInfoSet) {
						kind = item.getKindLoad();
						nameDisc = item.nameDisc;
						
						isInfoSet = true;
					}
					
					if (item.getValueEP() != 0) {
						valueEP = item.getValueEP();
					}					
					
					if (item.getTeacherId() != 0 && item.isAppointed()) {
						fio = DBManager.getInstance().getTeacherById(item.getTeacherId());
						isAppointed = true;
					} else if (!item.isAppointed()) {
						isAppointed = false;
					}
					
					valueG += item.getValueG();
					valueC += item.getValueCF() + item.getValueCO();
				}
				
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
