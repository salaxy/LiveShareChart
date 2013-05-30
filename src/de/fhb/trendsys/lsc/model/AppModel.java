package de.fhb.trendsys.lsc.model;

import javafx.scene.chart.XYChart;

/**
 * Das Model beinhaltet immer die aktuellen Chart-Daten.
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class AppModel {

	private XYChart.Series<Number, Number> dataSeries;

	public XYChart.Series<Number, Number> getDataSeries() {
		return dataSeries;
	}

	public AppModel() {
		super();
		init();
	}

	private void init() {

		dataSeries = new XYChart.Series<Number, Number>();
		dataSeries.setName("bleliebige Aktie");

		// create some data
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

	}

}
