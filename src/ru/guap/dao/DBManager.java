package ru.guap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.guap.dao.DBManager;

public class DBManager {
	public static final String DB_NAME = "kafedra";
	public static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

	public static final String MAIN_TABLE_NAME = "kaf43";
	public static final String LOADS_TABLE_NAME = "load";

	public static final String DB_LOGIN = "kafedra";
	public static final String DB_PASS = "123456";

	private static DBManager instance = null;
	private Connection con = null;

	private PreparedStatement psTeacher;

	private DBManager() {
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(URL, DB_LOGIN, DB_PASS);
				
				psTeacher = con.prepareStatement("SELECT fio FROM kafedra.teachers WHERE id = ?;");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}

		return instance;
	}

	public boolean executeSql(String sql) {
		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.executeUpdate();

		} catch (Exception ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null,
					ex);
			return false;
		}

		return true;
	}

	public ResultSet getSqlResult(String sql) {
		ResultSet res;

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			res = ps.executeQuery();

		} catch (Exception ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null,
					ex);
			res = null;
		}

		return res;
	}

	public int getLastInsertIdAfterSQL(String sql, String table) {
		int id = 0;

		try {
			Statement ps = con.createStatement();

			System.out.println(ps.toString());
			ps.execute(sql);
			ps.execute(String.format("SELECT LAST_INSERT_ID() FROM `%s`.`%s`;", DBManager.DB_NAME, table));

			ResultSet res = ps.getResultSet();
			if (res != null && res.next()) {
				id = res.getInt(1);
			}

		} catch (Exception ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null,
					ex);
			id = 0;
		}

		return id;	
	}

	public Connection getConnection() {
		return this.con;
	}

	public String getTeacherById(int teacherID) throws SQLException {
		psTeacher.setInt(1, teacherID);

		ResultSet res = psTeacher.executeQuery();

		while (res.next()) {
			String teacherName = res.getString(1);
			if (teacherName.trim().isEmpty()) {
				teacherName = "<не назначен>";
			}
			return teacherName;
		}    	

		return "<не назначен>";
	}
}
