package ru.guap.reports;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;





import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(description = "Generates and sends .xls report to user", urlPatterns = { "/GetReport" })
public class CreateReportServlet extends HttpServlet implements
javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	private String filePath;

	public void init() {
		// the file data.xls is under web application folder
		filePath = getServletContext().getRealPath("") + File.separator;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		int type = Integer.parseInt(request.getParameter("type"));
		
		sendFile(ReportFileGenerator.getInstance().generateReportFile(filePath, type), response);
	}

	private void sendFile(File file, HttpServletResponse response) {
		int length   = 0;
		try {
			ServletOutputStream outStream = response.getOutputStream();
			ServletContext context  = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(filePath);			
			
			// sets response content type
			if (mimetype == null) {
				mimetype = "application/octet-stream";
			}
			
			response.setContentType(mimetype);
			response.setContentLength((int)file.length());
			String fileName = (new File(filePath)).getName() + ".xls";

			// sets HTTP header
			response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			byte[] byteBuffer = new byte[BUFSIZE];
			DataInputStream in;
			in = new DataInputStream(new FileInputStream(file));

			// reads the file's bytes and writes them to the response stream
			while ((in != null) && ((length = in.read(byteBuffer)) != -1))
			{
				outStream.write(byteBuffer,0,length);
			}

			in.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
}
