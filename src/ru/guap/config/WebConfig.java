package ru.guap.config;

public class WebConfig {
    public static String DBF_UPLOAD_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "uploads";
    
    public static String JSP_PREFIX = "/Kafedra";
    
    public static String PAGE_NAME_MAIN = JSP_PREFIX + "/Main.jsp";
    public static String PAGE_NAME_ERROR = JSP_PREFIX + "/Error.jsp";
    public static String PAGE_NAME_UPLOAD = JSP_PREFIX + "/Upload.jsp";
    public static String PAGE_NAME_VIEW = JSP_PREFIX + "/View.jsp";
    
    public static enum Error { ERROR_DEFAULT, DBF_ERROR };
    
    public static String getErrorPage(Error e) {
	switch (e) {
		case DBF_ERROR:
		    return PAGE_NAME_ERROR + "?m=1";
		    
		case ERROR_DEFAULT:
		default:
		    return PAGE_NAME_ERROR + "?m=0";	
	}
    }
}
