package ru.guap.appointment;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import ru.guap.dao.DBManager;
import ru.guap.treeview.BurdenManager;
import ru.guap.treeview.GroupLoadItem;
import ru.guap.treeview.GroupStream;

public class AppointmentsManager {
	private Connection cnn;
	private PreparedStatement psAppointTeacher;
	
	private static AppointmentsManager instance;
	
	private AppointmentsManager() {
		cnn = DBManager.getInstance().getConnection();

		try {
			psAppointTeacher = cnn.prepareStatement("UPDATE kafedra.kaf43 SET teachers_id = ? WHERE id = ? AND load_id = " + BurdenManager.LOAD_VERSION);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static AppointmentsManager getInstance() {
		if (instance == null) {
			instance = new AppointmentsManager();
		} 
		
		return instance;
	}
	
	public int appointTeacherToStream(int streamId, int discId, int teacherId) throws SQLException {
		GroupStream s = BurdenManager.getInstance().getStreamForDiscAndId(streamId, discId);
		
		if (s == null) {
			return 0;
		}
		
		for (GroupLoadItem item : s.getItems()) {
			appointTeacherToItem(item.getId(), teacherId);
			
			item.setAppointed((teacherId != 0));
			item.setTeacherId(teacherId);
		}
		
		return discId + 23 * streamId;
	}
	
	public void appointTeacherToItem(int itemId, int teacherId) throws SQLException {
		if (teacherId == 0) {
			psAppointTeacher.setNull(1, Types.INTEGER);
		} else {
			psAppointTeacher.setInt(1, teacherId);
		}
		
		psAppointTeacher.setInt(2, itemId);
		
		psAppointTeacher.executeUpdate();		
	}
}
