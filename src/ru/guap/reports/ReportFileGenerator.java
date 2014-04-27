package ru.guap.reports;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import ru.guap.dao.DBManager;
import ru.guap.treeview.TreeNodeFactory;


public class ReportFileGenerator {
	public static final int REPORT_TYPE_AUTUMN_ONLY = 0;
	public static final int REPORT_TYPE_SPRING_ONLY = 1;
	public static final int REPORT_TYPE_A_AND_S = 2; // autumn and spring

	private static final int[] colWidths = { 50, 10, 10, 5, 5, 20 };
	private static final String[] headerSeason = { "Имя дисциплины", "Группа", "Тип занятий", "Часы Бюджет", "Часы контракт", "Преподаватель" };
	private static final String[] fieldsSeason = { "NameDisc", "Group", "KindLoad", "ValueG", "ValueCO", "teachers_id" };
	private static final Class[] typesSeason = { String.class, String.class, String.class, Integer.class, Integer.class, String.class };
	
	private static final int teacherField = fieldsSeason.length - 1;
	
	private static ReportFileGenerator instance;

	private ReportFileGenerator() {

	}

	public static ReportFileGenerator getInstance() {
		if (instance == null) {
			instance = new ReportFileGenerator();
		}

		return instance;
	}

	public File generateReportFile(String dir, int type) {
		switch (type) {
		case REPORT_TYPE_AUTUMN_ONLY:
			File fA = generateSeasonReport(true);
			return fA;

		case REPORT_TYPE_SPRING_ONLY:
			File fS = generateSeasonReport(false);
			return fS;

		case REPORT_TYPE_A_AND_S:
			File fAll = generateAllReport();
			return fAll;

		default:
			return null;
		}
	}

	private File generateSeasonReport(boolean isAutumn) {
		try {
			File outputXls = File.createTempFile("kafedra_" + System.currentTimeMillis(), ".xls");
			WritableWorkbook workbook = null;
			
			try {
				WorkbookSettings ws = new WorkbookSettings();	
				workbook = Workbook.createWorkbook(outputXls, ws); 

				WritableSheet sheet = workbook.createSheet((isAutumn) ? "Отчёт осень" : "Отчёт весна", 0); 
				
				// Create header of table
				for (int i = 0; i < this.headerSeason.length; i++) {
					sheet.setColumnView(i, this.colWidths[i]);
					Label l = new Label(i, 0, this.headerSeason[i]);
					sheet.addCell(l);
				}
				
				ResultSet res = selectSeason(isAutumn);
				
				int y = 1; // Start after header
				while (res.next()) {
					for (int i = 0; i < this.fieldsSeason.length; i++) {
						String fieldName = this.fieldsSeason[i];
						Class fieldType = this.typesSeason[i];
						
						if (fieldType == String.class) {
							String cell = "";
							if (i == this.teacherField) {
								cell = DBManager.getInstance().getTeacherById(res.getInt(fieldName));
							} else {
								cell = res.getString(fieldName);
							}
							
							Label l = new Label(i, y, cell);
							sheet.addCell(l);
						} else if (fieldType == Integer.class) {
							jxl.write.Number n = new jxl.write.Number(i, y, res.getInt(fieldName));
							sheet.addCell(n);
						}
					}
					
					y++;
				}
				
				return outputXls;
				
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				workbook.write();
				try {
					workbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;
	}

	private File generateAllReport() {
		try {
			File outputXls = File.createTempFile("kafedra_" + System.currentTimeMillis(), ".xls");
			WritableWorkbook workbook = null;
			
			try {
				WorkbookSettings ws = new WorkbookSettings();	
				workbook = Workbook.createWorkbook(outputXls, ws); 

				WritableSheet sheet = workbook.createSheet("Полный отчет", 0); 
				
				// Create header of table
				for (int i = 0; i < this.headerSeason.length; i++) {
					Label l = new Label(i, 0, this.headerSeason[i]);
					sheet.addCell(l);
				}
				
				ResultSet res = selectSeason(true);
				
				int y = 1; // Start after header
				while (res.next()) {
					for (int i = 0; i < this.fieldsSeason.length; i++) {
						String fieldName = this.fieldsSeason[i];
						Class fieldType = this.typesSeason[i];
						
						if (fieldType == String.class) {
							String cell = "";
							if (i == this.teacherField) {
								cell = DBManager.getInstance().getTeacherById(res.getInt(fieldName));
							} else {
								cell = res.getString(fieldName);
							}
							
							Label l = new Label(i, y, cell);
							sheet.addCell(l);
						} else if (fieldType == Integer.class) {
							jxl.write.Number n = new jxl.write.Number(i, y, res.getInt(fieldName));
							sheet.addCell(n);
						}
					}
					
					y++;
				}
				
				res = selectSeason(false);
				
				while (res.next()) {
					for (int i = 0; i < this.fieldsSeason.length; i++) {
						String fieldName = this.fieldsSeason[i];
						Class fieldType = this.typesSeason[i];
						
						if (fieldType == String.class) {
							String cell = "";
							if (i == this.teacherField) {
								cell = DBManager.getInstance().getTeacherById(res.getInt(fieldName));
							} else {
								cell = res.getString(fieldName);
							}
							
							Label l = new Label(i, y, cell);
							sheet.addCell(l);
						} else if (fieldType == Integer.class) {
							jxl.write.Number n = new jxl.write.Number(i, y, res.getInt(fieldName));
							sheet.addCell(n);
						}
					}
					
					y++;
				}				
				
				return outputXls;
				
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				workbook.write();
				try {
					workbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;
	}	
	
	private ResultSet selectSeason(boolean isAutumn) throws SQLException {
		Connection cnn = DBManager.getInstance().getConnection();

		PreparedStatement ps = null;
		if (isAutumn) {
			ps = cnn.prepareStatement("SELECT * FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (load_id=?)");
		} else {
			ps = cnn.prepareStatement("SELECT * FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (load_id=?)");
		}

		ps.setInt(1, TreeNodeFactory.LOAD_VERSION);

		return ps.executeQuery();
	}
}
