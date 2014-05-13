package ru.guap.barchart;

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
import ru.guap.treeview.BurdenManager;

@WebServlet("/PercentBarChart")
public class PercentBarChart extends BarChart {

	private PreparedStatement countValues;


	public PercentBarChart() {
		super();

		try {
			this.countValues = cnn.prepareStatement("SELECT sum(ValueG), sum(ValueCO), sum(ValueEP) FROM kafedra.kaf43 WHERE load_id = ? AND teachers_id = ?");
			this.teacherData = cnn.prepareStatement("SELECT * FROM kafedra.teachers");     
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
			int percent;
			countValues.setInt(1, BurdenManager.LOAD_VERSION);
			ResultSet tData = teacherData.executeQuery();

			while(tData.next()) {
				teachId = tData.getInt(1);
				teachName = tData.getString(2);
				teachRateG = tData.getInt(3);
				teachRateC = tData.getInt(4);
				this.countValues.setInt(2, teachId);
				ResultSet allValues = this.countValues.executeQuery();

				if(allValues.next()) {
					aVgO = allValues.getInt(2);
					aVcO = allValues.getInt(1); 

					percent = (aVgO + aVcO) / annualState * (teachRateG + teachRateC);
					dataset.addValue(percent, STR_PERCENT,teachName);					
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
			e.printStackTrace();
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
