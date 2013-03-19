package hr.fer.zemris.apr.lab4;

import hr.fer.zemris.apr.lab1.Matrix;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart extends JFrame {

	private static final long serialVersionUID = 2824883642090203610L;
	private DefaultCategoryDataset dataset;
	
	public Chart(List<Matrix> m, double[] T, String title) {
		dataset = fillDataset(m, T);
		
		JFreeChart chart = ChartFactory.createLineChart(title, "t", "x",
				dataset, PlotOrientation.VERTICAL, true, true, false);
				
		chart.setAntiAlias(true);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(169, 200, 255));
		plot.setRangeGridlinePaint(Color.black);
		plot.setDomainGridlinePaint(Color.black);


		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));

		setContentPane(chartPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private DefaultCategoryDataset fillDataset(List<Matrix> m, double[] T) {
		DefaultCategoryDataset d = new DefaultCategoryDataset();
		for (int i = 0; i < m.size(); i++) {
			double[][] e = m.get(i).getElements();
			try {
				for (int j = 0; j < e.length; j++) {
					d.addValue(e[j][0], "X" + Integer.toString(j),
							Double.toString(T[i]));
				}
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				System.out.println("X.size: " + m.size() + ", axis.size: "
						+ T.length);
			}
		}
		return d;
	}
}
