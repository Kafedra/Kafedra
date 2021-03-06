package ru.guap.barchart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import ru.guap.dao.DBManager;

public abstract class BarChart extends HttpServlet {
	protected static final int BLOCK_WIDTH = 580;
	protected static final int BLOCK_HEIGHT = 270;
	
	protected static final String STR_CONTRACT = "��������";
	protected static final String STR_BUDGET = "������";
	protected static final String STR_PERCENT = "�������";

	protected static int teachId;
	protected static int teachRateG; //rate budget
	protected static int teachRateC; // reate Contract
	protected static int aVgO; //Budget hours of current group
	protected static int aVcO; //Contract hours of current group
	protected static String teachName;
	
	protected static Connection cnn;
	protected static PreparedStatement teacherData;
	
	public BarChart() {
		super();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			cnn = DriverManager.getConnection(DBManager.URL, DBManager.DB_LOGIN, DBManager.DB_PASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
