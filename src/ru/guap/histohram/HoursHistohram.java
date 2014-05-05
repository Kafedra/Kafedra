package ru.guap.histohram;

import java.io.IOException;
import java.io.OutputStream;

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

@WebServlet("/HoursHistohram")
public class HoursHistohram extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HoursHistohram() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    		OutputStream out = response.getOutputStream();
    		try {
    		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    		dataset.addValue(10.0, "S1", "C1");
    		dataset.addValue(4.0, "S1", "C2");
    		dataset.addValue(15.0, "S1", "C3");
    		dataset.addValue(14.0, "S1", "C4");
    		dataset.addValue(-5.0, "S2", "C1");
    		dataset.addValue(-7.0, "S2", "C2");
    		dataset.addValue(14.0, "S2", "C3");
    		dataset.addValue(-3.0, "S2", "C4");
    		dataset.addValue(6.0, "S3", "C1");
    		dataset.addValue(17.0, "S3", "C2");
    		dataset.addValue(-12.0, "S3", "C3");
    		dataset.addValue( 7.0, "S3", "C4");
    		dataset.addValue(7.0, "S4", "C1");
    		dataset.addValue(15.0, "S4", "C2");
    		dataset.addValue(11.0, "S4", "C3");
    		dataset.addValue(0.0, "S4", "C4");
    		dataset.addValue(-8.0, "S5", "C1");
    		dataset.addValue(-6.0, "S5", "C2");
    		dataset.addValue(10.0, "S5", "C3");
    		dataset.addValue(-9.0, "S5", "C4");
    		dataset.addValue(9.0, "S6", "C1");
    		dataset.addValue(8.0, "S6", "C2");
    		dataset.addValue(null, "S6", "C3");
    		dataset.addValue(6.0, "S6", "C4");
    		dataset.addValue(-10.0, "S7", "C1");
    		dataset.addValue(9.0, "S7", "C2");
    		dataset.addValue(7.0, "S7", "C3");
    		dataset.addValue(7.0, "S7", "C4");
    		dataset.addValue(11.0, "S8", "C1");
    		dataset.addValue(13.0, "S8", "C2");
    		dataset.addValue(9.0, "S8", "C3");
    		dataset.addValue(9.0, "S8", "C4");
    		dataset.addValue(-3.0, "S9", "C1");
    		dataset.addValue(7.0, "S9", "C2");
    		dataset.addValue(11.0, "S9", "C3");
    		dataset.addValue(-10.0, "S9", "C4");
    		JFreeChart chart = ChartFactory.createBarChart(
    		"Bar Chart",
    		"Category",
    		"Value",
    		dataset,
    		PlotOrientation.VERTICAL,
    		true, true, false
    		);
    		response.setContentType("image/png");
    		ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
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
