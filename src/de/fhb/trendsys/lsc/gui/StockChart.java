package de.fhb.trendsys.lsc.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
import de.fhb.trendsys.lsc.db.control.BusinessLogic;
import de.fhb.trendsys.lsc.model.AppModel;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet,
 * Sie beinhaltet GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class StockChart extends Application {

	private BusinessLogic logic;

	private AppModel model;

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	@Override
	public void start(Stage stage) throws Exception {
		this.init(stage);
		stage.show();
	}

	private void init(Stage stage) {
		
		model = new AppModel();
		logic = new BusinessLogic(model);

		Group root = new Group();
		stage.setScene(new Scene(createChart()));
//		root.getChildren().add(createChart());
		
//		 Scene scene  = new Scene(lineChart,800,600); 

		// TODO Andy - timeline / animation

		
		System.out.println("Refreshing...");
		logic.refresh(1);
		System.out.println("Refresh finished.");
	}

	protected LineChart<String, Number> createChart() {

		xAxis = new CategoryAxis();
//				(0, 20, 1);;
		xAxis.setLabel("Time");
		yAxis = new NumberAxis();
//		yAxis = new NumberAxis(0, 100, 1);

		LineChart<String, Number> lineChart = new LineChart<String, Number>(
				xAxis, yAxis);

		// linechrt config
		lineChart.setId("Stockchart");
//		lineChart.setCreateSymbols(false);
//		lineChart.setAnimated(false);
		lineChart.setLegendVisible(true);
		lineChart.setTitle("Live Share Chart");
		xAxis.setLabel("Zeit");
//		xAxis.setForceZeroInRange(false);
		yAxis.setLabel("Preis");
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$",
				null));

		// Daten mit linechart verknuepfen - Databinding
		lineChart.getData().add(model.getDataSeries());

		return lineChart;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
