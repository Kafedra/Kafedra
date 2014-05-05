package ru.guap.histohram;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import ru.guap.dao.DBManager;
import ru.guap.treeview.TreeNodeFactory;

@WebServlet("/HoursHistohram")
public class HoursHistohram extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */	
	private static Connection cnn;
	private static PreparedStatement countValues,teacherData;//(1)Id,Имя - преподавателя (2)контракт,бюджет,
	
    public HoursHistohram() {
        super();
        cnn = DBManager.getInstance().getConnection();
        try {
        	countValues=cnn.prepareStatement("SELECT sum(ValueG),sum(ValueCO) FROM kafedra.kaf43 WHERE load_id = ? AND teachers_id = ? ");
        	teacherData=cnn.prepareStatement("SELECT * FROM kafedra.teachers");        	

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    		OutputStream out = response.getOutputStream();
    		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    		try {
    			int teachId = -1; //init
    			countValues.setInt(1,TreeNodeFactory.LOAD_VERSION);
    			ResultSet tData=teacherData.executeQuery();    			
    			while(tData.next()){
    				teachId=tData.getInt(1);
    				countValues.setInt(2,teachId);
    				ResultSet allValues=countValues.executeQuery();
    				if(allValues.next()){
	    				dataset.addValue(allValues.getInt(2), "Контракт", tData.getString(2));
	    				dataset.addValue(allValues.getInt(1), "Бюджет", tData.getString(2));    			
	    			}
    			}
    			JFreeChart chart = ChartFactory.createBarChart(
		    		"Нагрузка преподавателей в часах",
		    		"Соотношения",
		    		"Часы",
		    		dataset,
		    		PlotOrientation.VERTICAL,
		    		true, true, false
	    		);
	    		response.setContentType("image/png");
	    		ChartUtilities.writeChartAsPNG(out, chart, 580, 270);
	    		}
    		catch (Exception e) {
	    		System.err.println(e.toString());
	    		}
    		finally {
    			out.close();
    		}
    		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
