package de.fhb.trendsys.lsc.model;

import javafx.scene.chart.XYChart;

/**
 * Das Model beinhaltet immer die aktuellen Chart-Daten.
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class AppModel {

	private XYChart.Series<String, Number> dataSeries;

	public XYChart.Series<String, Number> getDataSeries() {
		return dataSeries;
	}

	public AppModel() {
		super();
		init();
	}

	private void init() {

		dataSeries = new XYChart.Series<String, Number>();
		dataSeries.setName("bleliebige Aktie");

		// create some data
//		dataSeries.getData().add(new XYChart.Data(1, 22));
//		dataSeries.getData().add(new XYChart.Data(2, 13));
//		dataSeries.getData().add(new XYChart.Data(3, 16));
//		dataSeries.getData().add(new XYChart.Data(4, 23));
//		dataSeries.getData().add(new XYChart.Data(5, 35));
//		dataSeries.getData().add(new XYChart.Data(6, 33));
//		dataSeries.getData().add(new XYChart.Data(7, 24));
//		dataSeries.getData().add(new XYChart.Data(8, 44));
//		dataSeries.getData().add(new XYChart.Data(9, 40));
//		dataSeries.getData().add(new XYChart.Data(10, 16));
//		dataSeries.getData().add(new XYChart.Data(11, 28));
//		dataSeries.getData().add(new XYChart.Data(12, 22));
		
		
//		dataSeries.getData().add(new XYChart.Data("Jan", 23));
//        dataSeries.getData().add(new XYChart.Data("Feb", 14));
//        dataSeries.getData().add(new XYChart.Data("Mar", 15));
//        dataSeries.getData().add(new XYChart.Data("Apr", 24));
//        dataSeries.getData().add(new XYChart.Data("May", 34));
//        dataSeries.getData().add(new XYChart.Data("Jun", 36));
//        dataSeries.getData().add(new XYChart.Data("Jul", 22));
//        dataSeries.getData().add(new XYChart.Data("Aug", 45));
//        dataSeries.getData().add(new XYChart.Data("Sep", 43));
//        dataSeries.getData().add(new XYChart.Data("Oct", 17));
//        dataSeries.getData().add(new XYChart.Data("Nov", 29));
//        dataSeries.getData().add(new XYChart.Data("Dec", 25));

	}
	
	
	
	

}
