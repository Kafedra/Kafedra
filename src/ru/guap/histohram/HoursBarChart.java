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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import ru.guap.dao.DBManager;
import ru.guap.treeview.TreeNodeFactory;

@WebServlet("/HoursBarChart")
public class HoursBarChart extends BarChart {

	
    public HoursBarChart() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    		OutputStream out = response.getOutputStream();
    		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    		try {
    			int teachId;
    			
    			countValues.setInt(1, TreeNodeFactory.LOAD_VERSION);
    			ResultSet tData = teacherData.executeQuery();    			
    			
    			while(tData.next()) {
    				teachId = tData.getInt(1);
    				countValues.setInt(2, teachId);
    				ResultSet allValues = countValues.executeQuery();
    				
    				if(allValues.next()) {
	    				dataset.addValue(allValues.getInt(2), STR_CONTRACT, tData.getString(2));
	    				dataset.addValue(allValues.getInt(1), STR_BUDGET, tData.getString(2));    			
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
    			
    			CategoryAxis ca = new CategoryAxis();
    			ca.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
    			ca.setMaximumCategoryLabelWidthRatio(5f);
    			
    			ca.setLowerMargin(0);
    			ca.setCategoryMargin(0);
    			ca.setUpperMargin(0);      			
    			
    			chart.getCategoryPlot().setDomainAxis(ca);
    			
	    		response.setContentType("image/png");
	    		ChartUtilities.writeChartAsPNG(out, chart, BLOCK_WIDTH, BLOCK_HEIGHT);
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
