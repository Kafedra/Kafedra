package ru.guap.barchart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import ru.guap.dao.DBManager;

public abstract class BarChart extends HttpServlet {
	protected static final int BLOCK_WIDTH = 580;
	protected static final int BLOCK_HEIGHT = 270;
	
	protected static final String STR_CONTRACT = "��������";
	protected static final String STR_BUDGET = "������";
	
	protected static Connection cnn;
	protected static PreparedStatement teacherData;
	
	public BarChart() {
		super();
		
        cnn = DBManager.getInstance().getConnection();
        
        try {
        	teacherData = cnn.prepareStatement("SELECT * FROM kafedra.teachers");        	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
