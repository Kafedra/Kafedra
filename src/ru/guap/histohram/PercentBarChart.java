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
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import ru.guap.dao.DBManager;
import ru.guap.treeview.TreeNodeFactory;

@WebServlet("/PercentBarChart")
public class PercentBarChart extends BarChart {

	public PercentBarChart() {
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
			int aVgO; //budget for cheking to zero
			int aVcO; //contract for cheking to zero
			int mustBe; 

			countValues.setInt(1, TreeNodeFactory.LOAD_VERSION);
			ResultSet tData = teacherData.executeQuery();

			while(tData.next()) {
				teachId = tData.getInt(1);
				countValues.setInt(2, teachId);
				ResultSet allValues = countValues.executeQuery();

				if(allValues.next()) {
					aVgO = allValues.getInt(2);
					aVcO = allValues.getInt(1); 
					mustBe = allValues.getInt(3);

					if((aVgO <= 0 && aVcO <= 0) || mustBe <= 0) {
						dataset.addValue(0, STR_CONTRACT, tData.getString(2));
						dataset.addValue(0, STR_BUDGET, tData.getString(2));   						
					}
					else if(aVgO <= 0){
						dataset.addValue(0, STR_CONTRACT, tData.getString(2));
						dataset.addValue((aVcO * 100f) / mustBe, STR_BUDGET, tData.getString(2)); 
					}
					else if (aVcO <= 0){

						dataset.addValue((aVgO * 100f) / mustBe, STR_CONTRACT, tData.getString(2));
						dataset.addValue(0, STR_BUDGET, tData.getString(2));     						
					}
					else {
						dataset.addValue((aVgO * 100f) / mustBe, STR_CONTRACT, tData.getString(2));
						dataset.addValue((aVcO * 100f) / mustBe, STR_BUDGET, tData.getString(2)); 
					}
				}
			}

			JFreeChart chart = ChartFactory.createBarChart(
					"Нагрузка преподавателей в процентах",
					"Соотношения",
					"Проценты",
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
