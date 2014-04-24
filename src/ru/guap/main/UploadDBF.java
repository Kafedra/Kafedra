package ru.guap.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import ru.guap.dao.dbf.DBFConverter;

@WebServlet(description = "A DBF file uploader", urlPatterns = { "/dbfupload" })
@MultipartConfig
public class UploadDBF extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UploadDBF() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.sendRedirect(WebConfig.getErrorPage(WebConfig.Error.ERROR_DEFAULT));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

            PrintWriter out = response.getWriter();
            HttpSession httpSession = request.getSession();
            String filePathUpload = (String) httpSession.getAttribute("path")!=null ? httpSession.getAttribute("path").toString() : WebConfig.DBF_UPLOAD_PATH ;

            System.out.println("Path: " + filePathUpload);
            
            String path1 =  filePathUpload;
            String filename = null;
            File path = null;
            FileItem item=null;


            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                String FieldName = "";
                try {
                    Map<String, List<FileItem>> i = upload.parseParameterMap(request);
                    List items = i.get("dbf");
                    
                    Iterator iterator = items.iterator();
                    while (iterator.hasNext()) {
                        item = (FileItem) iterator.next();

                        if (!item.isFormField()) {
                            filename = System.currentTimeMillis() + "_" + item.getName();
                            path = new File(path1 + File.separator);
                            
                            if (!path.exists()) {
                                boolean status = path.mkdirs();
                            }
                            
                            File uploadedFile = new File(path + System.getProperty("file.separator") + filename);  // for copy file
                            item.write(uploadedFile);

                            boolean result = DBFConverter.insertDBFContentToDB(uploadedFile);
                            if (!result) {
                        	response.sendRedirect(WebConfig.getErrorPage(WebConfig.Error.DBF_ERROR));
                        	return;
                            }
                            
                            response.sendRedirect(WebConfig.PAGE_NAME_MAIN);
                            return;
                        }

                    } // END OF WHILE 
                    
                    response.sendRedirect(WebConfig.PAGE_NAME_MAIN);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }   
    }
}
