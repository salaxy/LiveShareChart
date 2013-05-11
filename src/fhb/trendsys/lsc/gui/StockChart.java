package fhb.trendsys.lsc.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StockChart extends Application{
	
	private XYChart.Series<Number, Number> dataSeries;
	
	private NumberAxis xAxis;
	private NumberAxis yAxis;

	@Override
	public void start(Stage stage) throws Exception {
		
		//second method called after start
		this.init(stage);
		stage.show();
		
	}
	
	private void init(Stage stage) {
		
		//first method called after start

		Group root = new Group();

		stage.setScene(new Scene(root));

		root.getChildren().add(createChart());

		//TODO  timeline / animation


	}
	
	protected LineChart<Number, Number> createChart() {

		xAxis = new NumberAxis(0, 20, 1);

		yAxis = new NumberAxis(0, 100, 1);

		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(
				xAxis, yAxis);

		

		lineChart.setId("Stockchart");

		lineChart.setCreateSymbols(false);

		lineChart.setAnimated(false);

		lineChart.setLegendVisible(false);

		lineChart.setTitle("Live Share Chart");

		xAxis.setLabel("Zeit");
//		xAxis.setForceZeroInRange(false);

		yAxis.setLabel("Preis");

		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$",
				null));

		//sample data
		
		dataSeries = new XYChart.Series<Number, Number>();

		dataSeries.setName("bleliebige Aktie");
		
		//....

		// create some starting data
		
		dataSeries.getData().add(new XYChart.Data(1, 22));
		dataSeries.getData().add(new XYChart.Data(2, 13));
		dataSeries.getData().add(new XYChart.Data(3, 16));
        dataSeries.getData().add(new XYChart.Data(4, 23));
        dataSeries.getData().add(new XYChart.Data(5, 35));
        dataSeries.getData().add(new XYChart.Data(6, 33));
        dataSeries.getData().add(new XYChart.Data(7, 24));
        dataSeries.getData().add(new XYChart.Data(8, 44));
        dataSeries.getData().add(new XYChart.Data(9, 40));
        dataSeries.getData().add(new XYChart.Data(10, 16));
        dataSeries.getData().add(new XYChart.Data(11, 28));
        dataSeries.getData().add(new XYChart.Data(12, 22));

		
		//Daten mit linechart verknuepfen
		
		lineChart.getData().add(dataSeries);

		return lineChart;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
