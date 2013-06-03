package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.XYChart;

/**
 * Das Model beinhaltet immer die aktuellen Chart-Daten.
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class AppModel {

	private XYChart.Series<String, Number> dataSeries;
	
	private List<XYChart.Series<String, Number>> avaiableStockData = new ArrayList<XYChart.Series<String, Number>>();
	
	private String actualStock=" AktieXY";
	
	private String actualNewsFeeds= " News ... +++";
	
	private String actualOpenedNewsURL = "http://www.finanzen.net/nachricht/aktien/China-und-Costa-Rica-unterzeichnen-Wirtschaftsabkommen-2469118";
	
	private String contentTag ="<div class=\"content\">";
	//TODO evtl. herraus-parsen des inhaltes, wenn moeglich und zeit is

	public XYChart.Series<String, Number> getDataSeries() {
		return dataSeries;
	}

	public String getActualStock() {
		return actualStock;
	}

	public void setActualStock(String actualStock) {
		this.actualStock = actualStock;
	}

	public String getActualNewsFeeds() {
		return actualNewsFeeds;
	}

	public void setActualNewsFeeds(String actualNewsFeeds) {
		this.actualNewsFeeds = actualNewsFeeds;
	}

	public String getActualOpenedNewsURL() {
		return actualOpenedNewsURL;
	}

	public void setActualOpenedNewsURL(String actualOpenedNewsURL) {
		this.actualOpenedNewsURL = actualOpenedNewsURL;
	}

	public AppModel() {
		super();
		init();
	}
	
	public List<String> getStockNames(){
		
		List<String> nameList = new ArrayList<String>();
		
		for(XYChart.Series<String, Number> series : avaiableStockData){
			nameList.add(series.getName());
		}
		
		return nameList;
	}

	private void init() {

		dataSeries = new XYChart.Series<String, Number>();
		dataSeries.setName("AktieXY");
		avaiableStockData.add(dataSeries);

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
		
		
		dataSeries.getData().add(new XYChart.Data("Jan", 23));
        dataSeries.getData().add(new XYChart.Data("Feb", 14));
        dataSeries.getData().add(new XYChart.Data("Mar", 15));
        dataSeries.getData().add(new XYChart.Data("Apr", 24));
        dataSeries.getData().add(new XYChart.Data("May", 34));
        dataSeries.getData().add(new XYChart.Data("Jun", 36));
        dataSeries.getData().add(new XYChart.Data("Jul", 22));
        dataSeries.getData().add(new XYChart.Data("Aug", 45));
        dataSeries.getData().add(new XYChart.Data("Sep", 43));
        dataSeries.getData().add(new XYChart.Data("Oct", 17));
        dataSeries.getData().add(new XYChart.Data("Nov", 29));
        dataSeries.getData().add(new XYChart.Data("Dec", 25));
        
        
        
        
        this.actualNewsFeeds = "Java FX 2.0 News "  + " ++++ "
        +" tomorrow 85 °F and sunny :)" + " ++++ "
		+  "DAX	8.241	-1,3% " + " ++++ "
		+ " Dow	15.116	-1,4% " + " ++++ "
		+ " Euro	1,3021	0,2%" + " ++++ "
		+ " ESt50	2.738	-1,1% " + " ++++ "
		+ " Nas	3.456	-1,0% " + " ++++ "
		+ " Öl	100,1	0,0%"+ " ++++ " 
		+ "TDax	949,7	-1,6%  " + " ++++ "
		+ " Nikkei	13.262	-3,7%  	" + " ++++ "
		+ " Gold	1.399	0,8%";
        
        
        
        

	}
	
	
	
	

}
